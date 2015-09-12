package org.iatoki.judgels.api.sandalphon;

import org.iatoki.judgels.api.JudgelsClientAPI;

import java.io.InputStream;

public interface SandalphonClientAPI extends JudgelsClientAPI {

    SandalphonProblem findProblemByJid(String problemJid);

    String getProblemStatementRenderAPIEndpoint(String problemJid);

    String getProblemStatementMediaRenderAPIEndpoint(String problemJid, String mediaFilename);

    String constructProblemStatementRenderAPIRequestBody(String problemJid, SandalphonProblemStatementRenderRequestParam param);

    SandalphonLesson findLessonByJid(String lessonJid);

    String getLessonStatementRenderAPIEndpoint(String lessonJid);

    String getLessonStatementMediaRenderAPIEndpoint(String lessonJid, String mediaFilename);

    String constructLessonStatementRenderAPIRequestBody(String lessonJid, SandalphonLessonStatementRenderRequestParam param);

    SandalphonProgrammingProblemInfo getProgrammingProblemInfo(String problemJid);

    InputStream downloadProgrammingProblemGradingFiles(String problemJid);

    SandalphonBundleGradingResult gradeBundleProblem(String problemJid, SandalphonBundleAnswer answer);
}
