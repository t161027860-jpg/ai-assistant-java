package com.example.aiassistant;

public class Constants {
    public static final String PREFS = "ai_assistant_prefs";
    public static final String KEY_GROQ_API = "groq_api_key";
    public static final String KEY_MODEL = "groq_model";
    public static final String KEY_WAKE_WORD = "wake_word";
    public static final String KEY_LANG = "language";
    public static final String KEY_SYSTEM_PROMPT = "system_prompt";

    public static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    public static final String DEFAULT_MODEL = "llama3-8b-8192";
    public static final String DEFAULT_WAKE_WORD = "окей ассистент";
    public static final String DEFAULT_LANG = "ru-RU";
    public static final String DEFAULT_PROMPT =
        "Ты голосовой помощник в наушнике. Отвечай ОЧЕНЬ кратко — максимум 2 предложения.";

    public static final int REQ_SPEECH = 100;
    public static final int REQ_PERMISSIONS = 101;
}
