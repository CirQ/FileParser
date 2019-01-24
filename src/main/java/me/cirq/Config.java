package me.cirq;

import me.cirq.util.ProjectPathHandler;

/**
 * Common config among all context in source classes.
 */
public interface Config {

    String PROJECT_PATH = "";

    ProjectPathHandler PATH_HANDLER = new ProjectPathHandler(PROJECT_PATH);

}
