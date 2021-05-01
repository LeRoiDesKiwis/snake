package fr.leroideskiwis.snake.utils;

import fr.leroideskiwis.snake.entities.Body;
import fr.leroideskiwis.snake.entities.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityUtils {
    public static Optional<Body> getHead(List<Entity> entities){
        return getEntity(entities, entity -> entity instanceof Body && ((Body)entity).isType(Body.BodyType.HEAD))
                .map(entity -> (Body)entity);
    }

    public static Optional<Entity> getEntity(List<Entity> entities, Predicate<Entity> predicate){
        return entities.stream().filter(predicate).findAny();
    }

    private static List<Entity> getEntitiesCopy(List<Entity> entities){
        return new ArrayList<>(entities);
    }

    public static  List<Entity> getFullEntities(List<Entity> entities){
        return getEntitiesCopy(entities).stream()
                .flatMap(entity1 -> {
                    if(entity1 instanceof Body && ((Body)entity1).isType(Body.BodyType.HEAD)){
                        return ((Body)entity1).toList(null).stream();
                    }
                    return Stream.of(entity1);
                }).collect(Collectors.toList());
    }
}
