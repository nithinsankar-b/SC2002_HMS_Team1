# SC2002_HMS_Team1

# <a id="hospitalManagementSystem"> Hospital Management System (HMS)</a>

![UML Class Diagram](https://img.shields.io/badge/UML%20Class%20Diagram-1976D2?style=for-the-badge&logoColor=white)
![Solid Design Principles](https://img.shields.io/badge/SOLID%20Design%20Principles-C71A36?style=for-the-badge&logoColor=white)
![OOP Concepts](https://img.shields.io/badge/OOP%20Concepts-C71A36?style=for-the-badge&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)

**Team:** [<img src="https://avatars.githubusercontent.com/u/119853913?v=4" height="20" width="20" /> Zhan You](https://github.com/donkey-king-kong) |
[<img src="https://avatars.githubusercontent.com/u/144437711?v=4" height="20" width="20" /> Nithin](https://github.com/nithinsankar-b) |
[<img src="https://avatars.githubusercontent.com/u/167533024?v=4" height="20" width="20" /> Janhavee](https://github.com/JanhaveeSingh) |
[<img src="https://avatars.githubusercontent.com/u/164110710?v=4" height="20" width="20" /> Risha](https://github.com/RISHASUN001) |
[<img src="https://avatars.githubusercontent.com/u/164110710?v=4" height="20" width="20" /> Chang Lin (Link & Picture not updated)](https://github.com/RISHASUN001)

**Docs:** [Report](https://github.com/xJQx/sc2002-fypms/blob/main/report.pdf) |
[UML Class Diagram](https://github.com/xJQx/sc2002-fypms/blob/main/uml%20class%20diagram/uml-class-diagram.jpg) |
[Java Docs](https://xjqx.github.io/sc2002-fypms/sc2002_fypms/module-summary.html)

> - A Java-based application for managing our `Hospital Management System` for `doctors`, `patients`, `pharmacists` and `administrators`. 
Some features include authentication, different users management, appointment management between doctors and patients, medicine inventory management between pharmacists and administrator . 
>- This README file provides instructions on how to clone, compile, and run the project.

---

## <a id="tableContents">Table of Contents</a>

- [SC2002\_HMS\_Team1](#sc2002_hms_team1)
- [ Hospital Management System (HMS)](#-hospital-management-system-hms)
  - [Table of Contents](#table-of-contents)
- [HMS setup instructions](#hms-setup-instructions)
  - [Compiling and Running the project](#compiling-and-running-the-project)
    - [Through the terminal](#through-the-terminal)
    - [Using Eclipse](#using-eclipse)
  - [Generating JavaDocs](#generating-javadocs)
    - [Using the terminal](#using-the-terminal)
    - [Using Eclipse](#using-eclipse-1)
- [Usage](#usage)
  - [Login Credentials](#login-credentials)

---

# <a id="setupInstructions">HMS setup instructions</a>

## Compiling and Running the project

### Through the terminal

The following instructions will guide you through the process of cloning the repository, navigating to the cloned repository, compiling the project, and running the project in your terminal.

1. Open your terminal
2. Clone the repository by entering the following command:

   ```
   git clone https://github.com/softwarelab3/2006-SDAD-6.git
   ```

3. Navigate to the cloned repository by entering the following command:

   ```
   cd "2006-SDAD-6"
   ```

4. Compile the project by entering the following command:

   ```
   javac -cp src -d bin src/main.java
   ```

5. Run the project by entering the following command:

   ```
   java -cp bin main.main
   ```

Congratulations! You have just successfully cloned, compiled, and run the HMS project!

### Using Eclipse

If you prefer to use Eclipse as your IDE, you can also set up the project there. Here are the steps you need to follow:

1. Open Eclipse
2. Click on `File` > `Import` > `Git` > `Projects from Git` > `Clone URI`
3. In the `Clone URI` window, paste the following URL:

   ```bash
   https://github.com/xJQx/sc2002-fypms.git
   ```

4. Click `Next` and follow the prompts to finish the cloning process
5. Once the project is cloned, right-click on the project folder and select `Properties`
6. In the `Properties` window, click on `Java Build Path` > `Source` > `Add Folder`
7. Select the `src` folder from the project directory and click `OK`
8. Now you can run the project by right-clicking on `FypmsApp.java` in the `src/main` folder and selecting `Run As` > `Java Application`

That's it! You should now have the project up and running in Eclipse.

## Generating JavaDocs

### Using the terminal

Follow the steps below to generate JavaDocs using the terminal:

1. Open you terminal.
2. Navigate to the root directory of the project.
3. Run the following command in the terminal:

   ```bash
    javadoc -d docs -sourcepath src -subpackages controllers:enums:interfaces:main:models:services:stores:utils:views -private
   ```

   This command will generate the JavaDocs and save them in the docs directory in HTML format.

4. Navigate to the `docs` directory using the following command:

   ```bash
   cd docs
   ```

5. Open the `index.html` file in a web browser to view the generated JavaDocs.

Congratulations, you have successfully created and viewed the JavaDocs.

### Using Eclipse

1. Open the Eclipse IDE and open your Java project.

2. Select the package or class for which you want to generate JavaDocs.

3. Go to the "Project" menu and select "Generate Javadoc".

4. In the "Generate Javadoc" dialog box, select the "Private" option to generate JavaDocs for private classes and members.

5. Choose the destination folder where you want to save the generated JavaDocs.

6. In the "Javadoc command line arguments" field, add any additional arguments that you want to include, such as `-classpath`.

7. Click the "Finish" button to start the JavaDocs generation process.

8. Once the JavaDocs have been generated, you can view them by opening the `index.html` file in your web browser.

Congratulations, you have successfully created and viewed the JavaDocs.

# Usage

## Login Credentials

This section contains some login credentials for users with different roles. The full list is available in `data/user.csv` file.

**Students:**

```bash
# Student 1
USERID: YCHERN
PASSWORD: password

# Student 2
USERID: KOH1
PASSWORD: password

# Student 3
USERID: CT113
PASSWORD: password
```

**Supervisors:**

```bash
# Supervisor 1
USERID: BOAN
PASSWORD: password

# Supervisor 2
USERID: LIMO
PASSWORD: password
```

**FYP Coordinator:**

```bash
# FYP Coordinator 1
USERID: ASFLI
PASSWORD: password
```
