package agh.ics.generator.mapelements.animal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Genotype {
    private final List<Integer> genotype;
    Random random = new Random();

    //constructor creates a random genotype
    public Genotype() {
        this.genotype = random.ints(32, 0, 7).boxed().sorted()
                .collect(Collectors.toList());;
    }

    //constructor creates a proportional genotype
    public Genotype(List<Integer> leftGenotype, List<Integer> rightGenotype,int lengthLeft){
        this.genotype = createGenotype(leftGenotype,rightGenotype,lengthLeft);
    }

    public List<Integer> getGenotype(){
        return this.genotype;
    }

    public List<Integer> createGenotype(List<Integer> leftGenotype, List<Integer> rightGenotype,int lengthLeft){
        List<Integer> genotype = new ArrayList<>();

        for(int i = 0;i < lengthLeft;i++){
            genotype.add(leftGenotype.get(i));
        }
        for(int i = lengthLeft; i < rightGenotype.size(); i++){
            genotype.add(rightGenotype.get(i));
        }
        Collections.sort(genotype);

        return genotype;
    }
}
