package com.ariesninja.renderstorm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL45.*;

public class Shader {
    private int programID;

    public Shader(String vertexPath, String fragmentPath) throws IOException {
        String vertexCode = new String(Files.readAllBytes(Paths.get(vertexPath)));
        String fragmentCode = new String(Files.readAllBytes(Paths.get(fragmentPath)));

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexCode);
        glCompileShader(vertexShader);
        checkCompileErrors(vertexShader, "VERTEX");

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentCode);
        glCompileShader(fragmentShader);
        checkCompileErrors(fragmentShader, "FRAGMENT");

        programID = glCreateProgram();
        glAttachShader(programID, vertexShader);
        glAttachShader(programID, fragmentShader);
        glLinkProgram(programID);
        checkCompileErrors(programID, "PROGRAM");

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void use() {
        glUseProgram(programID);
    }

    public void setInt(String name, int value) {
        glUniform1i(glGetUniformLocation(programID, name), value);
    }

    private void checkCompileErrors(int shader, String type) {
        int success;
        if (type.equals("PROGRAM")) {
            success = glGetProgrami(shader, GL_LINK_STATUS);
            if (success == GL_FALSE) {
                String infoLog = glGetProgramInfoLog(shader);
                System.out.println("ERROR::SHADER::PROGRAM::LINKING_FAILED\n" + infoLog);
            }
        } else {
            success = glGetShaderi(shader, GL_COMPILE_STATUS);
            if (success == GL_FALSE) {
                String infoLog = glGetShaderInfoLog(shader);
                System.out.println("ERROR::SHADER::" + type + "::COMPILATION_FAILED\n" + infoLog);
            }
        }
    }

    public void setMat4(String name, float[] matrix) {
        glUniformMatrix4fv(glGetUniformLocation(programID, name), false, matrix);
    }
}