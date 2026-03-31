-- Importing package(s)
with Ada.Text_IO;
use Ada.Text_IO;

-- Custom package(s)
with complex_numbers;
use complex_numbers;

-- Essentially the Main method
procedure Main is
   -- Setting up variables

   -- Provided data the user can modify through the system
   Complex_Number1 : Values := (Real => 3.14, Complex => 2.71);
   Complex_Number2 : Values := (Real => 1.61, Complex => 1.41);

   -- Used for printing results after an operation
   Result : Values;

   Input : Integer;

   Running : Boolean := True;


-- Logic begins here

begin
   -- System loop
   while Running loop
      -- Printing menu
      Put_Line ("=== Complex Number Calculator ===");
      Put ("First complex number is: "); Put_Custom_Float (Complex_Number1.Real); Put (" + ("); Put_Custom_Float (Complex_Number1.Complex); Put(" )i");
      New_Line;
      Put ("Second complex number is: "); Put_Custom_Float (Complex_Number2.Real); Put (" + ("); Put_Custom_Float (Complex_Number2.Complex); Put(" )i");
      New_Line; New_Line;
      Put_Line ("1. Change first complex number");
      Put_Line ("2. Change second complex number");
      Put_Line ("3. Print sum");
      Put_Line ("4. Print difference");
      Put_Line ("5. Print product");
      Put_Line ("6. Print quotient");
      Put_Line ("7. Quit program");
      New_Line;
      Put ("Enter a number to choose an action: ");

      -- Input is parsed into Integer
      begin
         Input := Integer'Value(Get_Line);

      -- All instances of numerical inputs in Main have exception handling
      -- In case of exception, print error message(s) and skip this loop iteration
      -- Skipping iteration is done by going to a specific line in 'Main'
      exception
         when Constraint_Error =>
            Put_Line ("Error: Invalid input!");
            goto Skip_Iteration;
      end;

      if Input = 1 then
         -- Local variables created to hold input values
         declare
            Real : Float;
            Complex : Float;

         begin
            Put ("Enter the new real value: ");
            Real := Float'Value(Get_Line);
            Put ("Enter the new complex value: ");
            Complex := Float'Value(Get_Line);

            -- Local variables used only when user enters all valid inputs
            -- Sets Complex_Number1's sub-values to those of the local variables'
            Complex_Number1.Real := Real;
            Complex_Number1.Complex := Complex;

         exception
            when Constraint_Error =>
               Put_Line ("Error: Invalid input!");
               Put_Line ("(no changes made)");
               goto Skip_Iteration;
         end;

      -- Identical to Input = 1, but for Complex_Number2
      elsif Input = 2 then
         declare
            Real : Float;
            Complex : Float;

         begin
            Put ("Enter the new real value: ");
            Real := Float'Value(Get_Line);
            Put ("Enter the new complex value: ");
            Complex := Float'Value(Get_Line);

            Complex_Number2.Real := Real;
            Complex_Number2.Complex := Complex;

         exception
            when Constraint_Error =>
               Put_Line ("Error: Invalid input!");
               Put_Line ("(no changes made)");
               goto Skip_Iteration;
         end;

      -- Input = 3, 4, 5, and 6 follow a similar pattern
      -- They each call their respective calculation as indicated by the menu
      -- Then their outputs are printed out
      elsif Input = 3 then
         Result := Calculate_Sum (Complex_Number1, Complex_Number2);
         Put ("Sum: "); Put_Custom_Float (Result.Real); Put (" + ( "); Put_Custom_Float (Result.Complex); Put(" )i");
         New_Line;

      elsif Input = 4 then
         Result := Calculate_Difference (Complex_Number1, Complex_Number2);
         Put ("Difference: "); Put_Custom_Float (Result.Real); Put (" + ( "); Put_Custom_Float (Result.Complex); Put(" )i");
         New_Line;

      elsif Input = 5 then
         Result := Calculate_Product (Complex_Number1, Complex_Number2);
         Put ("Product: "); Put_Custom_Float (Result.Real); Put (" + ( "); Put_Custom_Float (Result.Complex); Put(" )i");
         New_Line;

      elsif Input = 6 then
         Result := Calculate_Quotient (Complex_Number1, Complex_Number2);
         Put ("Qutient: "); Put_Custom_Float (Result.Real); Put (" + ( "); Put_Custom_Float (Result.Complex); Put(" )i");
         New_Line;

      -- Loop is exited, which reaches the ends the program
      elsif Input = 7 then
         Put_Line ("Quitting program...");
         Running := False;

      else
         Put_Line ("Error: Invalid input!");

      end if;

      -- Where to go when a loop needs to be skipped for whatever reason
      -- Mainly used by exception handling blocks
      << Skip_Iteration >>
      New_Line;

   end loop;

   null;
end Main;
