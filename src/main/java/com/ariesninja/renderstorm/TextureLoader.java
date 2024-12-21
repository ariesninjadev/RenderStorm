package com.ariesninja.renderstorm;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class TextureLoader {

    private static TextureLoader instance;

    public static TextureLoader getGlobal() {
        return instance;
    }

    String baseDir;
    String packDataPath;

    HashMap<Integer, String> textureMap = new HashMap<>();

    public TextureLoader(String baseDir, String packDataPath) {
        if (instance != null) {
            throw new RuntimeException("TextureLoader already exists");
        }
        instance = this;

        this.baseDir = baseDir;
        this.packDataPath = packDataPath;

        // Read each line of the pack data file txt and load the texture. Format: textureID=texturePath
        try {
            BufferedReader reader = new BufferedReader(new FileReader(baseDir + packDataPath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                textureMap.put(Integer.parseInt(parts[0]), parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int loadTexture(int texture) {

        // Load texture from pack data
        String filePath = baseDir + textureMap.get(texture);

        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Load image
        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true); // Flip image vertically
            ByteBuffer image = STBImage.stbi_load(filePath, width, height, channels, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load texture: " + STBImage.stbi_failure_reason());
            }

            // Upload texture to GPU
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);

            STBImage.stbi_image_free(image);
        }

        return textureID;
    }
    
}