package it.polimi.sw.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectiveTest{

    private Objective objective;
    private Conditions conditions;

    @BeforeEach
    void setUp() {
        objective = new Objective(5, new Conditions(), true);
        conditions = new Conditions(1, 3, ObjectiveTypes.ResourceFilling, (Arrays.asList(Resources.FUNGI)), (Arrays.asList(Object.MANUSCRIPT)), "Red", "Blue", 1, "Collect 3 Red Resources");
    }

    @Test
    void testObjectiveConstructors() {
        Objective defaultObjective = new Objective();
        assertNotNull(defaultObjective);
        assertEquals(0, defaultObjective.getPoints());
        assertFalse(defaultObjective.isChosen());
        assertNull(defaultObjective.getCondition());

        Objective paramObjective = new Objective(10, conditions, false);
        assertEquals(10, paramObjective.getPoints());
        assertEquals(conditions, paramObjective.getCondition());
        assertFalse(paramObjective.isChosen());
    }

    @Test
    void testObjectiveGettersSetters() {
        objective.setPoints(15);
        assertEquals(15, objective.getPoints());

        objective.setChosen(true);
        assertTrue(objective.isChosen());

        objective.setCondition(new Conditions());
        assertNotNull(objective.getCondition());


    }

    @Test
    void testObjectiveTypes() {
        ObjectiveTypes[] types = ObjectiveTypes.values();
        assertEquals(4, types.length);
        assertEquals("ResourceFilling", types[0].name());
        assertEquals("ObjectFilling", types[1].name());
        assertEquals("ThreeSameColourPlacing", types[2].name());
        assertEquals("TwoSameColourOneDiffersPlacing", types[3].name());
    }

    @Test
    void testConditionsConstructors() {
        Conditions defaultConditions = new Conditions();
        assertNotNull(defaultConditions);
        assertNull(defaultConditions.getId());
        assertNull(defaultConditions.getQuantityRequiredToObtainPoints());
        assertNull(defaultConditions.getTypeOfObjective());
        assertNull(defaultConditions.getResourcesRequiredToObtainPoints());
        assertNull(defaultConditions.getObjectsRequiredToObtainPoints());
        assertNull(defaultConditions.getColour1());
        assertNull(defaultConditions.getColour2());
        assertNull(defaultConditions.getFirstOrientationCornerCheck());
        assertNull(defaultConditions.getDescription());

        Conditions paramConditions = new Conditions(1, 3, ObjectiveTypes.ResourceFilling, (Arrays.asList(Resources.FUNGI)), (Arrays.asList(Object.MANUSCRIPT)), "Red", "Blue", 1, "Collect 3 Red Resources");
        assertEquals(1, paramConditions.getId());
        assertEquals(3, paramConditions.getQuantityRequiredToObtainPoints());
        assertEquals(ObjectiveTypes.ResourceFilling, paramConditions.getTypeOfObjective());
        assertEquals("Collect 3 Red Resources", paramConditions.getDescription());


    }

    @Test
    void testConditionsGettersSetters() {
        conditions.setId(2);
        assertEquals(2, conditions.getId());

        conditions.setQuantityRequiredToObtainPoints(4);
        assertEquals(4, conditions.getQuantityRequiredToObtainPoints());

        conditions.setTypeOfObjective(ObjectiveTypes.ObjectFilling);
        assertEquals(ObjectiveTypes.ObjectFilling, conditions.getTypeOfObjective());

        conditions.setDescription("threeColourPlacing");
        assertEquals("threeColourPlacing",conditions.getDescription());
    }
    @Test
    void testToString(){
        objective.toString().compareTo("Objective{" +
                "points=" + objective.getPoints() +
                ", condition=" + objective.getCondition().toString() +
                ", chosen=" + objective.isChosen() +
                '}' +"\n");
    }
}
