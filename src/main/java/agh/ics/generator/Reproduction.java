package agh.ics.generator;

import java.util.*;

public class Reproduction {
    protected final AbstractWorldMap map;
    protected final int minimumEnergy;
    Random random = new Random();

    public Reproduction(AbstractWorldMap map) {
        this.map = map;
        this.minimumEnergy = this.map.startEnergy/2;
    }

    public void doReproduction(HashMap<Vector2d, List<Animal>> animalsOnMap){
        for(List<Animal> animals : animalsOnMap.values()) {
            animals.sort((o1, o2) -> {
                return Integer.compare(o2.getEnergy(), o1.getEnergy());
            });

            if (animals.size() >= 2) {
                createChildren(new ArrayList<>(Arrays.asList(animals.get(0), animals.get(1))));
            }
        }

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
        return animalsForReproduction.get(0).getEnergy() >= minimumEnergy && animalsForReproduction.get(1).getEnergy() >= minimumEnergy;
    }

    public int calculateLengthDominantGene(List<Animal> animalsForReproduction){
        double proportion = ((double) animalsForReproduction.get(1).getEnergy())/(animalsForReproduction.get(0).getEnergy() + animalsForReproduction.get(1).getEnergy());
        return (int) (animalsForReproduction.get(0).getGenes().size()*proportion);
    }

    public void lostEnergy(Animal animal){
        animal.setEnergy(animal.getEnergy()- animal.getEnergy()/4);
    }

    public void createChildren(List<Animal> animalsForReproduction){
        Genotype genotype;
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

        lostEnergy(animalsForReproduction.get(0));
        lostEnergy(animalsForReproduction.get(1));

        animalsForReproduction.get(0).updateNumberOfChildren();
        animalsForReproduction.get(1).updateNumberOfChildren();

        Animal newAnimal = new Animal(this.map,
                animalsForReproduction.get(0).getEnergy()/4 + animalsForReproduction.get(1).getEnergy()/4,
                genotype,animalsForReproduction.get(0).getPosition());
        this.map.place(newAnimal);
    }

}
