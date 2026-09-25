package com.bytebites.config;

import java.util.HashMap;
import java.util.Map;

import com.cloudinary.Cloudinary;

public class CloudinaryConfig {

    public static Cloudinary cloudinary;
    public static Cloudinary getCloudinary(){
        
        if(cloudinary == null){

            Map<String,Object> config = new HashMap<>();
            
            config.put("cloud_name","rvvhylqw");
            config.put("api_key","562691333871154");
            config.put("api_secret","ryVQ0zKFAyXlXQOYJOGY_9khmtU");
            config.put("secure","true");

            cloudinary = new Cloudinary(config);
        }
        return cloudinary;
    }    
}