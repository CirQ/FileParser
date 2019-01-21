#! /usr/bin/python3
# coding: utf-8


from git import Repo
import re
import json
import git
import datetime

#read crash buckets
def getBuckets(dir):
    with open(dir,encoding='utf-8') as f:
        b = [json.loads(line) for line in f.readlines()]
    return b

# git show format
def getShow(dir,commit):
    repo = Repo(dir)
    dics=[]
    string = re.split('diff --git ', repo.git.show(commit))
    del (string[0])
    for s in string:
        ss = s.split('\n')
        pre = ss[2][6:]
        del (ss[0:4])
        j = 0
        k = 0  # 初始行数
        for line in ss:
            if line.startswith('@@ -'):
                digit = re.findall(r"\d+\.?\d*", line)
                j = int(digit[0])
                k = int(digit[0])

            elif line.startswith('+'):
                dic = {
                    "commit": str(commit),
                    "file": pre,
                    "change": '+',
                    "line": str(j),
                    "context": line[1:]
                }
                # json.dump(dic,f)
                dics.append(dic)
                j = j + 1

            elif line.startswith('-'):
                dic = {
                    "commit": str(commit),
                    "file": pre,
                    "change": '-',
                    "line": str(k),
                    "context": line[1:]
                }
                dics.append(dic)
                k = k + 1
            else:
                j = j + 1
                k = k + 1
    return dics

# Inverse Average Distance to Crashing Point
def getIADCP(bucket,revision):
    val=0

    for crash in bucket['stack_traces']:
        min=10000
        crash_line=crash['stack_frames'][0]['line_number']
        crash_file=crash['stack_frames'][0]['file_name']

        for r in revision:
            if r['file'].split('/')[-1]==crash_file:
                distance = abs(r['line_number']-crash_line)
                if min>distance:
                    min=distance

        val+=min

    return 1/(1+(val/len(bucket)))

# Inverse Average Distance to Crashing Line
def getIADCL(bucket,revision):
    min=10000

    for crash in bucket['stack_traces']:
        for frame in crash['stack_frames']:
            crash_line = frame['line_number']
            crash_file = frame['file_name']

            for r in revision:
                if r['file'].split('/')[-1] == crash_file:
                    distance = abs(r['line_number'] - crash_line)
                    if min > distance:
                        min = distance

    return 1/(1+min)

# Revision Frequency
def getRF(buckets,m):
    d={}
    count=0
    for bucket in buckets:
        for crash in bucket['stack_traces']:
            for frame in crash['stack_frames']:
                method=frame['method']
                if method not in d.keys():
                    d[method]=1
                else:
                    d[method]+=1
                count+=1

    # j=json.dump(d)

    return d[m]/count

# Affected Line
def getAffectedLineNum(dir,commit):
    revision = getShow(dir, commit)
    return len(revision)

# Rank Commit by date
def getCommitDate(commit_id,dir):
    repo = git.Repo(dir)
    dt = repo.git.log("--pretty=format:%ct", commit_id).split('\n')[0]
    return (dt, commit_id)


def ts2date(ts):
    return datetime.datetime.utcfromtimestamp(ts)


def sortCommitByDate(commit_ids):
    rank = []
    for id in commit_ids:
        rank.append(getCommitDate(id))
    return sorted(rank)

def getPoint(commit_candidate,crash_dir,git_dir,bucket):
    d={}
    count=1

    rank=sortCommitByDate(commit_candidate)

    for dt,commit in rank:
        point=0
        revision=getShow(git_dir,commit)
        point+=getIADCL(bucket,revision)
        point+=getIADCP(bucket,revision)
        #ITDCR point
        point+=1/(1+count)

        d[commit]=point/3

    return d





# if __name__ == "__main__":
    # crash_dir = 'bug_report'
    # git_dir = 'hadoop'
    # buckets = getBuckets(dir)
    #
    # getPoint([], crash_dir, git_dir, buckets[0])