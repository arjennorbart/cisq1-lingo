package nl.hu.cisq1.lingo.trainer.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import nl.hu.cisq1.lingo.trainer.domain.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Hint {
    private List<Character> characters;

    public void replaceHint(List<Character> newCharacters) {
        List<Character> replacementList = new ArrayList<>();
        for (int i = 0; i < this.characters.size(); i++) {
            if(this.characters.get(i).equals(Utils.dot()) || this.characters.get(i).equals(Utils.plus()))
                replacementList.add(newCharacters.get(i));
            else replacementList.add(this.characters.get(i));
        }
        this.characters = replacementList;
    }
}
