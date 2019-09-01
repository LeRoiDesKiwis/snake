package fr.leroideskiwis.snake.entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Entities {

    private List<Entity> entities = new ArrayList<>();

    public void add(Entity entity){
        entities.add(entity);
    }

    public Iterator<Entity> iterator(){
        return entities.iterator();
    }

    public Optional<Entity> getEntity(Point point){
        return entities.stream().filter(entity -> entity.isLocation(point)).findAny();
    }

    public void foreach(Consumer<Entity> consumer){
        Iterator<Entity> iterator = iterator();

        while(iterator.hasNext()){
            Entity entity = iterator.next();
            consumer.accept(entity);
        }
    }

    public Stream<Entity> stream(){
        return entities.stream();
    }

    public Entity getEntity(Class<? extends Entity> clazz){

        return entities.stream().filter(entity -> entity.getClass() == clazz).findAny().orElse(null);

    }

    public boolean contains(Class<? extends Entity> clazz){
        return entities.stream()
                .anyMatch(entity -> entity.getClass() == clazz);
    }

}
