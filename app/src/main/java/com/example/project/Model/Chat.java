package com.example.project.Model;

import java.util.HashMap;
import java.util.Map;

public class Chat{
    public Map<String, Boolean> users = new HashMap<>(); // 채팅방유저들
    public Map<String, Comment> comments = new HashMap<>();

    public static class Comment{
     public String uid;
     public String message;
     public Object timestamp;
     public Map<String,Object> readUsers = new HashMap<>();
    }
}
