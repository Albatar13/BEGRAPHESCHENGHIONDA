# Graph & Algorithm project &mdash; INSA Toulouse

## How to start?

You will not be able to use this repository to save your work, you need to copy / import / fork it to 
your favorite Git platform.

### Importing to [Github](https://github.com), [Bitbucket](https://bitbucket.org) or [Gitlab](https://gitlab.com):

You first need to register and then log in to the platform you want. The steps to import the project are detailed below:

#### Github

1. In the upper-right corner of any page, click the **"+"** icon, then click **Import repository**, or go to [https://github.com/new/import](https://github.com/new/import). 
2. Paste the following URL in the first input:
     [https://gitea.typename.fr/INSA/be-graphes.git](https://gitea.typename.fr/INSA/be-graphes.git)
3. Choose the name you want for the repository.
4. Click *Begin import*.
5. Wait for completion... Done!

#### Bitbucket

1. On the left panel of any page, click the **"+"** icon, then **Repository**, and then **Import**, or directly go to [https://bitbucket.org/repo/import](https://bitbucket.org/repo/import). 
2. Paste the following URL in the first input (select Git as source if not already selected):
     [https://gitea.typename.fr/INSA/be-graphes.git](https://gitea.typename.fr/INSA/be-graphes.git)
3. Choose the name you want for repository, and select Git as the *Version control system*.
4. Click *Import repository*.
5. Wait for completion... Done!

#### Gitlab

1. In the upper-right corner of any page, click the **"+"** icon, then **New project**, or directly go to [https://gitlab.com/projects/new](https://gitlab.com/projects/new).
2. Select the **Import project** tab, and then click **Repo by URL** (right-most option).
3. Paste the following URL in the first input:
     [https://gitea.typename.fr/INSA/be-graphes.git](https://gitea.typename.fr/INSA/be-graphes.git)
4. Choose the name you want for the repository.
5. Click *Create project*.
6. Wait for completion... Done!

### Importing to another repository provider *[not recommended]*:

1. Create a new **empty** repository (no README, no LICENSE) on your provider. Let's assume the URL of your repository is `$URL_REPOSITORY`.
2. Clone this repository somewhere:

    ```bash
	git clone https://gitea.typename.fr/INSA/be-graphes.git
	```
    
3. Go inside the newly cloned repository and update the **remote**:
   
    ```bash
	cd be-graphes
	git remote set-url origin $URL_REPOSITORY
	```
    
4. Push to your repository:

    ```bash
	push -u origin master
	```
	
Another way is to do a bare clone and then mirror it to your repository: [https://help.github.com/articles/importing-a-git-repository-using-the-command-line/](https://help.github.com/articles/importing-a-git-repository-using-the-command-line/)