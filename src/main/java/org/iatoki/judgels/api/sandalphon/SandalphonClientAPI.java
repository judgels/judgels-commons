package org.iatoki.judgels.api.sandalphon;

import org.iatoki.judgels.api.JudgelsClientAPI;

import java.io.InputStream;

public interface SandalphonClientAPI extends JudgelsClientAPI {

    SandalphonProblem findProblemByJid(String problemJid);

    String getProgrammingProblemStatementRenderAPIEndpoint(String problemJid);

    String getBundleProblemStatementRenderAPIEndpoint(String problemJid);

    String constructProgrammingProblemStatementRenderAPIRequestBody(String problemJid, SandalphonProgrammingProblemStatementRenderRequestParam param);

    String constructBundleProblemStatementRenderAPIRequestBody(String problemJid, SandalphonBundleProblemStatementRenderRequestParam param);

    String getProblemStatementMediaRenderAPIEndpoint(String problemJid, String mediaFilename);

    SandalphonLesson findLessonByJid(String lessonJid);

    String getLessonStatementRenderAPIEndpoint(String lessonJid);

    String getLessonStatementMediaRenderAPIEndpoint(String lessonJid, String mediaFilename);

    String constructLessonStatementRenderAPIRequestBody(String lessonJid, SandalphonLessonStatementRenderRequestParam param);

    SandalphonProgrammingProblemInfo getProgrammingProblemInfo(String problemJid);

    InputStream downloadProgrammingProblemGradingFiles(String problemJid);

    SandalphonBundleGradingResult gradeBundleProblem(String problemJid, SandalphonBundleAnswer answer);
}
