* Primeira coisa a se fazer, clonar o projeto do GitHub, para isso deves abrir o git no seu terminal primeiro, crie uma nova pasta para por o projeto nela  e abra o Git nessa pasta

* Iniciar o Git no seu terminal
git init

* Clonar o projeto
git clone https://github.com/YesLap/Android.git

* Abrir o projeto e fazer as mudaças que você quiser, Depois de ter feita todas as suas mudnças e quiser mandar o projeto para o GitHub seguir os seguintes passos no Git:

* Criar uma nova branch
git checkout -b "nomedabranch" (para seguir um padrão colocar o nome da seguinte maneira, edit-nomedaactivity-seunome)

* depois basta dar uma add para adcioanr todos os arquivos
git add .

* depois basta dar um commit
git commit -m "versaodocommit" (esta versão voce tera que chegar la no repositorio do github (https://github.com/YesLap/Android/releases) qual foi a ultima versao lançada e acrescentar uma a mais, ex, se a ultima for 1.0.9 voce devera colcoar 1.0.10)

* depois disso devera voltar para a branch master
git checkout master

* depois disso iremos usar o rebase para juntar sua branch com a branch master
git rebase nomedasuabranch (ex: edit-userprofile-rodrigo)
git pull --rebase origin master
git pushorigin master

* depois disso acrescentar uma tag com o que foi feito no projeto
git tag -a 1.0.0 -m "mensagem" (aquele 1.0.0 devera ser substituido pela versao que voce commitou, e a mensagem devera descrever o que voce editou no projeto)

* enviar a tag para o github
git push origin master --tags
