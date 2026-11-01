# 9. Debugging tools - NetBeans, Eclipse, IntelliJ IDEA, Visual Studio Code

A debugger runs your program under control so you can stop it on a line, look at the variables
and run it one statement at a time. `DebugDemo` is a small marks report with a loop and four
methods, written to practise on.

## Things to try (same in every IDE)

1. Put a **breakpoint** on the line `int total = calculateTotal(marks[i]);` in `main` and start the debugger.
2. Look at the **Variables** panel: `i`, `names`, `marks`, `topperIndex`.
3. **Step into** `calculateTotal`. Step over the loop and watch `total` grow.
4. **Step out** and look at the **Call stack** (`main` -> `calculateTotal`).
5. Add `average` and `topperAverage` to the **Watch** list and continue to the next iteration.
6. Add a **conditional breakpoint** inside `hasPassedAll` on the line `return false;` with the
   condition `mark < 30` - it stops only for Kiran's 28.
7. In the debug console / evaluate window run `calculateAverage(total, 3)` or `marks[1][0] + 10`.

## Keys and menus

| Action | NetBeans | Eclipse | IntelliJ IDEA | VS Code |
|--------|----------|---------|---------------|---------|
| Start debugging | Debug Project `Ctrl+F5` | Debug As > Java Application `F11` | `Shift+F9` | `F5` |
| Toggle breakpoint | `Ctrl+F8` | `Ctrl+Shift+B` | `Ctrl+F8` | `F9` |
| Step over | `F8` | `F6` | `F8` | `F10` |
| Step into | `F7` | `F5` | `F7` | `F11` |
| Step out | `Ctrl+Alt+F7` | `F7` | `Shift+F8` | `Shift+F11` |
| Continue | `F5` | `F8` | `F9` | `F5` |
| Stop | `Shift+F5` | `Ctrl+F2` | `Ctrl+F2` | `Shift+F5` |
| Evaluate an expression | Watches window | Display / Expressions view | `Alt+F8` | Debug Console / Watch |
| Conditional breakpoint | right click the breakpoint > Properties | right click > Breakpoint Properties | right click the red dot | right click the gutter > Add Conditional Breakpoint |

## Where the panels are

- **NetBeans** - Variables, Watches, Call Stack and Breakpoints windows open at the bottom (Window > Debugging).
- **Eclipse** - the *Debug* perspective: Debug (call stack), Variables, Breakpoints and Expressions views.
- **IntelliJ IDEA** - the *Debug* tool window: Frames, Variables and Watches.
- **VS Code** - needs the *Extension Pack for Java*; the *Run and Debug* side bar shows Variables, Watch,
  Call Stack and Breakpoints. `.vscode/launch.json` in this folder already has the launch setting.

## Run without a debugger

```
mvn -q compile exec:java
```
