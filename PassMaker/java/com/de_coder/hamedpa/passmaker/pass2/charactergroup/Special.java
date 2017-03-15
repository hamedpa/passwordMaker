package com.de_coder.hamedpa.passmaker.pass2.charactergroup;
//Developed by Hamedpa

public class Special implements CharacterGroup {

    private static final String CHARACTERS = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    @Override
    public char[] getChars() {
        return CHARACTERS.toCharArray();
    }

}
