package GameTools;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target(ElementType.METHOD) // !Specifico su cosa può essere usata la notazione
public @interface Phase {
}

// !Voglio usarla sui metodi di GameManager per specificare che definiscono una
// phase. A questo punto mi conviene farne uno pur per gli effetti tipo @Action shuffle()
