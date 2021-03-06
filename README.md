**_ПОНИМАНИЕ JVM_**

***Java  Virtual Machine*** *(JVM,  Java  VM)*  —  виртуальная
машина Java, основная часть исполняющей системы JRE.

**Java-программа** - это набор текстовых файлов с расширением
.java и написанных в корректном синтаксисе.

Итак, у нас имеется следующий код, находящийся в файле **JvmComprehension.java:**
~~~Java
public class JvmComprehension {

    public static void main(String[] args) {
        int i = 1;                      
        Object o = new Object();        
        Integer ii = 2;                 
        printAll(o, i, ii);             
        System.out.println("finished"); 
    }

    private static void printAll(Object o, int i, Integer ii) {
        Integer uselessVar = 700;                   
        System.out.println(o.toString() + i + ii);  
    }
}
~~~

После работы компилятора создается файл, содержащий **bytecode** с расширением
**class** - **JvmComprehension.class**.

В данном файле, в том числе,
содержится:
* информация о версии Java;
* информация о формате конкретного class-файла;
* массив констант, в т.ч. информация о его размере и типах
используемых констант;
* информация о том, является этот файл классом или интерфейсом,
общедоступный он или абстрактный, является ли он финальным;
* информация о количестве полей в этом классе;
* массив переменных, причем только тех, что были объявлены
в данном классе;
* массив структур, в которые входит полное описание сигнатуры
используемых методов;
* дополнительная мета-информация, например имя файла, который
был скомпилирован.

В данном случае, у нас major версия 0x3D, что соответствует
Java SE 17, файл представляет собой обычный класс Java. 
Константы отсутствуют, имеется одно поле **i** примитивного
типа **int**, проинициализированное целым числом **"1":**
~~~Java
 int i = 1
~~~
поле **ii** класса-оболочки примитивного типа **Integer**, проинициализированное
целым числом **"2":**
~~~Java
Integer ii = 2;
~~~
поле **o** ссылочного типа класса **Object**, которому передан конструктор 
или ссылка на новый экземпляр класса **Object** в области памяти **Heap:**
~~~Java
Object o = new Object();
~~~
Кроме того, имеется основной метод **main** (точка входа) и метод
типа **void** **printAll** c аргументами целочисленного и ссылочного типа:
~~~Java
Object o, int i, Integer ii
~~~
и полем **uselessVar** типа **Integer:**
~~~Java
Integer uselessVar = 700;
~~~

Рассмотрим как наш класс обрабатывет JVM. Чтобы попасть в JVM,
класс должен быть загружен. Для этого существуют специальные
классы-загрузчики:
* **Bootstrap** - является базовым и 
загружает только платформенные классы Java (основные модули 
Java SE и JDK);
* **Platform** -  выполняет поиск модулей, 
определенных для всех встроенных загрузчиков;
* **Application** - используется для загрузки классов приложения
из classpath.

 
При загрузке файла **JvmComprehension.class** в JVM подсистема 
загрузчиков классов выделит в нашем скомпилированного байт-коде
три класса: пользовательский класс **JvmComprehension**, системные - 
классы **String**, **Object**, **System** и класс-оболочку **Integer**, 
при этом:
* все классы будут загружены сначала в **Application ClassLoader**,
который передаст их загрузчику **Platform ClassLoader**;
* **Platform ClassLoader**, в свою очередь, передаст их **Bootstrap ClassLoader**;
* **Bootstrap**, проверив их наличие в платформенной библиотеке
Java, загрузит в память классы **String**, **Object**, **Integer**, **System**,
а пользовательский класс **JvmComprehension** вернет загрузчику **Platform**;
* загрузчик **Platform**, проверив наличие класса **JvmComprehension** 
в дополнительных библиотеках, вернет его **Application ClassLoader**;
* загрузчик **Application** загрузит класс **JvmComprehension** в память.

![](/ClassLoaders.jpg)

После того, как классы были определены тем или иным загрузчиком классов, 
они загружаются в область памяти **MetaSpace**, в которой хранится 
вся информация уровня класса, такая как имя класса, имя непосредственного
родительского класса, информация о методах и переменных, включая 
статические переменные:

![](/MethodArea.jpg)

При этом осуществляется **процесс связывания Linking**, состоящий из:
* проверки, что код валиден - **Verify**;
* подготовки примитивов в статических полях - **Prepare**;
* связывания ссылок на другие классы - **Resolve**.

![](/Linking.jpg)

После того, как все загрузилось, происходит процесс инициализации классов
**Initialization**.

![](/Initialization.jpg)

Таким образом, наш класс JvmComprehension загружен в область памяти 
**MetaSpace**.

![](/MetaSpaceJvmComprehension.jpg)

Затем, так как у нас точкой входа является метод **main**, под него 
в области памяти **Stack Memory** создается фрейм памяти:
![](/МетодMain.jpg)
Затем по порядку во фрейме **main** создается переменная примитивного 
типа **int** **i** и ей присваивается целое значение **"1"** :
![](/IntI.jpg)
Далее в области памяти **Heap** конструктором **new** создается объект типа **Object** и в 
ссылочную переменную **o** записывается ссылка на этот объект:
![](/ОбъектO.jpg)
То же будет и с переменной **ii** типа обертки **Integer**, ей будет передано
значение ссылки на объект **Integer** в **Heap**, в котором храниться
значение целого числа **"2"**:
![](/IntegerII.jpg)
Далее под метод **printAll** в стеке будет выделен еще один фрейм:
![](/MethodPrintAll.jpg)
В этом фрейме будет созданы переменные ссылочного типа **Object o** и
**Integer ii**, которым будут присвоены значения ссылок на соответствующие
объекты в **Heap**, а также будет создана переменная целого типа **i**,
которой будет присвоено значение **"1"**:
![](/VarMethodPrintAll.jpg)
Далее в этом же фрейме будет создана ссылочная переменная **uselessVar** типа **Integer**,
которой будет присвоено значение ссылки на созданный в **Heap** объект, в котором 
хранится значение целого числа **"700"**:  
![](/VarUselessVar.jpg)
При вызове метода **println()** потока **out** класса **System**
в стеке создается отдельный фрейм, где в аргумент метода передается ссылка 
на строку, созданную в области памяти **Heap** - **String pool**, состоящую из
конкатенации значений метода **toString()** объекта **o**, переменных **i** и
**ii**:
![](/PrintLn.jpg)
После завершения выполнения метода **printAll()** следующим шагом выполняется 
вызов в **main** метода **println()**, которому передается ссылка на строку **"finished"**
в **String pool**:
![](/PrintlnMain.jpg)

**После выполнения последней строки кода стековая память для метода **main()**,
в том числе и метода **printAll()** будет уничтожена, Java Runtime освобождает
всю память и завершает программу.**

**Данные из Heap будут удаляться постепенно путем сбора объектов из памяти,
которые больше не используются.**







