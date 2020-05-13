# vassare-jimp

En vässare om JavaFX.

## Förberedelser

Installerad Java 11 (eller senare) JDK. Ett bra sätt om man har Bash är att använda https://sdkman.io/

Installera Scene Builder från https://gluonhq.com/products/scene-builder/

Valfri IDE för att skriva Java-kod.

## Agenda

* JavaFX historia
* JavaFX begrepp och terminologi (Controls, Containers, Layoutmanager, properties, controllers, FXML)
* Code-A-Long: Sätt upp projektet i IntelliJ & Scene Builder
* Paus
* Code-A-Long: Paintbrush
  * git checkout step1
* Code-A-Long: Image layering

# Historia

## v1.0
Released December 2008

"F3 is actually a declarative Java scripting language with static typing for good IDE support and compile-time error reporting (unlike JavaScript...), type-inference, declarative syntax, and automatic data-binding with full support for 2d graphics and standard Swing components as well as declarative animation. " https://web.archive.org/web/20120106124512/http://blogs.oracle.com/chrisoliver/entry/f3

## v1.1
Released February 2009

JavaFX for mobile development.

## v1.2 
Released June 2009

* Beta support for Linux and Solaris
* Built-in controls and layouts
* Skinnable CSS controls
* Built-in chart widgets

The acquisition of Sun Microsystems by Oracle Corporation was completed on January 27, 2010

## v2.0
Released October 2011

* Inget mer scriptspråk, rena Java APIer liknande Swing.
* FXML introduceras
* Oracle går ut med att de skall släppa JavaFX som OSS

## JavaFX 8
Released Marched 2014

Nu del av JRE 8.

## JavaFX 11
Released September 2018

"As of JDK 11 the JavaFX modules are delivered separately from the JDK." https://github.com/javafxports/openjdk-jfx/blob/jfx-11/doc-files/release-notes-11.md

Gluon https://gluonhq.com/ blir mer och mer de som syns mest vad gäller JavaFX

## JavaFX 14

Senaste 

# Terminologi

## Nodes


Instanser av UI-element, textrutor, knappar, menuer o.s.v.

                             --------
              ------        / Nodes /
             | VBox |       --------
              ------
                 |
        ------------------
        |                |
    ------------  -------------
    | Checkbox |  | TextField |
    ------------  -------------

Noder är hierarkiska.

I stora drag finns det två sorters noder: Containers - som hanterar layout av sina underliggande noder (children), och UI Controls - som är mindre bitar som textfält, knappar, ikoner o.s.v.

## Scene

Den mojäng som håller koll på nod-trädet (SceneGraph). 

## Stage

Håller koll på scenen och skapar fönstret (Window) som applikationen bor i.

## Properties

En definiution av Plain Old Java Beans Properties som man inte så ofta ser i Enterprise sammanhang:

>A bound property notifies listeners when its value changes. This has two implications:
>
>    The bean class includes addPropertyChangeListener() and removePropertyChangeListener() methods for managing the bean's listeners.
>    When a bound property is changed, the bean sends a PropertyChangeEvent to its registered listeners.

https://docs.oracle.com/javase/tutorial/javabeans/writing/properties.html

Från JavaFX 2.1

> This release introduces property support into JavaFX, support that is based on the proven JavaBeans model, but expanded and improved.
>
> JavaFX properties are often used in conjunction with binding, a powerful mechanism for expressing direct relationships between variables. When objects participate in bindings, changes made to one object will automatically be reflected in another object

https://docs.oracle.com/javafx/2/binding/jfxpub-binding.htm

Vilken är en hyfsat bekväm modell för att propagera värden från UI till sin modell.
