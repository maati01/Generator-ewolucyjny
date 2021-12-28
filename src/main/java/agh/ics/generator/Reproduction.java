package agh.ics.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Reproduction {
//    protected final List<Animal> animalsOnMap;
//    protected final List<Animal> animalsForReproduction;
    protected final AbstractWorldMap map;
    protected final int minimumEnergy = 5;
    Random random = new Random();

    public Reproduction(AbstractWorldMap map) {
//        this.animalsOnMap = animalsOnMap;
        this.map = map;
//        this.animalsForReproduction = findCandidatesForReproduction();
    }

    public List<Animal> findCandidatesForReproduction(List<Animal> animalsOnMap){
        animalsOnMap.sort((o1, o2) -> {
            return Integer.compare(o1.getEnergy(),o2.getEnergy());
        });

        return new ArrayList<>(Arrays.asList(animalsOnMap.get(0),animalsOnMap.get(1)));
    }

    public String pickSide(){
        int result = random.nextInt(2);

        if(result == 0){
            return "left";
        }else{
            return "right";
        }
    }

    public boolean checkEnergyLimits(List<Animal> animalsForReproduction){
        return animalsForReproduction.get(0).energy >= minimumEnergy && animalsForReproduction.get(1).energy >= minimumEnergy;
    }

    public int calculateLengthDominantGene(List<Animal> animalsForReproduction){
        double proportion = ((double) animalsForReproduction.get(1).energy)/(animalsForReproduction.get(0).energy + animalsForReproduction.get(1).energy);
        return (int) (animalsForReproduction.get(0).getGenes().size()*proportion);
    }

    public void createChildren(List<Animal> animalsOnMap){
        if(animalsOnMap.size() < 2){
            return;
        }

        Genotype genotype;
        List<Animal> animalsForReproduction = findCandidatesForReproduction(animalsOnMap);

        if(!checkEnergyLimits(animalsForReproduction)){
            return;
        }

        String side = pickSide();
        int length = calculateLengthDominantGene(animalsForReproduction);


        if(side.equals("left")){
            genotype = new Genotype(animalsForReproduction.get(0).getGenes(),
                    animalsForReproduction.get(1).getGenes(),length);
        }else{
            genotype = new Genotype(animalsForReproduction.get(1).getGenes(),
                    animalsForReproduction.get(0).getGenes(),animalsForReproduction.get(0).getGenes().size() - length);
        }

        this.map.place(new Animal(this.map,30,genotype));
    }

}
