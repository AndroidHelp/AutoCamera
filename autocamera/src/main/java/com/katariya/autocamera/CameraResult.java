package com.katariya.autocamera;

import java.io.File;

/**
 * Created by Hemant Katariya on 6/6/2017.
 */

public interface CameraResult {

    void success(String imagePath, File imageFile);
    void failure(String error);
}
