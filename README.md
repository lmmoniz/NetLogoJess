# NetLogoJess

1. What it is
   ----------

* NetLogoJess is a NetLogo extension that use Jess rule based 
  language to build and execute netlogo turtle behaviours.
  
* Jess is a rule engine and scripting environment written 
  entirely in Oracle's® Java™ language by Ernest Friedman-Hill 
  at Sandia National Laboratories in Livermore, CA.
  (http://www.jessrules.com/)
  
* NetLogo is a multi-agent programmable modeling environment. 
  It is authored by Uri Wilensky and developed at the CCL.
  (https://ccl.northwestern.edu/netlogo/)

2. System requirements
   -------------------
   
* NetLogo 6.0

* Java Runtime Environment 5

* Jess 7.0 
 
3. How to run it

* Create a folder <jessLogo> under the extensions folder of the NetLogo 
  application.
 
* Put <jessLogo.jar> and <jess.jar> files inside <jessLogo> folder

4. How to use it

* Load the extension on your NetLogo project
    <extensions [jessLogo]>
    
* Tou can execute any jess command using the <eval> instruction
  p.e. : show jesslogo:eval "(+ 1 2)"
