package com.de_coder.hamedpa.passmaker.pass2.charactergroup;
//Developed by Hamedpa

public class Lowercase implements CharacterGroup {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";

    @Override
    public char[] getChars() {
        return CHARACTERS.toCharArray();
    }
    
}
