package com.de_coder.hamedpa.passmaker.pass2.generator;
//Developed by Hamedpa

import com.de_coder.hamedpa.passmaker.pass2.charactergroup.CharacterGroup;

import java.util.Collection;

public interface PasswordGenerator {

    String generatePassword(int length, Collection<CharacterGroup> characterGroups);

}
