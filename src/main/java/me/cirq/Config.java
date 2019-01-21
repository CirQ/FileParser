package me.cirq;

public interface Config {

    String PROJECT_PATH = "";

    ProjectPathHandler PATH_HANDLER = new ProjectPathHandler(PROJECT_PATH);

}
