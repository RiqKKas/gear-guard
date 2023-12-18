#include <SoftwareSerial.h>
#include <Ultrasonic.h>

SoftwareSerial mySerial(11, 12); // Define os pinos RX e TX para comunicação serial
String comando = "";
bool indoParaFrente = false;
bool indoParaTras = false;

Ultrasonic ultrasonic(10, 9); // Inicializa o sensor ultrassônico nos pinos 10 e 9
long distancia = 0;
int velocidadeDireita = 70;  // Velocidade inicial da roda direita
int velocidadeEsquerda = 95; // Velocidade inicial da roda esquerda

void setup()
{
  // Configura os pinos como saídas
  pinMode(3, OUTPUT);
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
  pinMode(6, OUTPUT);
  pinMode(7, OUTPUT);
  pinMode(8, OUTPUT);

  Serial.begin(115200);
  mySerial.begin(9600); // Inicia a comunicação Serial com o Bluetooth
}

void loop()
{
  // Verifica se há dados disponíveis para leitura na porta Serial do Bluetooth
  if (mySerial.available())
  {
    comando = mySerial.readStringUntil('\n'); // Lê o comando enviado via Bluetooth
    interpretarComandoBluetooth(comando);     // Interpreta o comando recebido
  }

  distancia = ultrasonic.read(CM); // Lê a distância do obstáculo mais próximo em centímetros
  ajustarVelocidade();             // Ajusta a velocidade com base na distância do obstáculo
}

void ajustarVelocidade()
{
  // Ajusta a velocidade do robô baseando-se na distância do obstáculo
  if (!indoParaTras)
  {
    if (distancia < 20)
    {
      parar(); // Para o robô se o obstáculo estiver muito próximo
    }
    else if (distancia < 80)
    {
      setVelocidade(70, 95); // Define uma velocidade menor se o obstáculo estiver a uma distância média
    }
    else
    {
      setVelocidade(89, 126); // Define uma velocidade maior se não houver obstáculos próximos
    }
  }
}

void setVelocidade(int velocidadeDireitaAux, int velocidadeEsquerdaAux)
{
  // Ajusta a velocidade das rodas
  velocidadeDireita = velocidadeDireitaAux;
  velocidadeEsquerda = velocidadeEsquerdaAux;

  // Aplica a velocidade nas rodas
  analogWrite(3, velocidadeDireita);
  analogWrite(6, velocidadeEsquerda);
}

void interpretarComandoBluetooth(String cmd)
{
  // Interpreta os comandos recebidos via Bluetooth
  if (cmd == "frente")
  {
    moverParaFrente();
  }
  else if (cmd == "direita")
  {
    andarParaDireita();
  }
  else if (cmd == "esquerda")
  {
    andarParaEsquerda();
  }
  else if (cmd == "tras")
  {
    moverParaTras();
  }
  else if (cmd == "parar")
  {
    parar();
  }
}

void moverParaFrente()
{
  // Define a movimentação do robô para frente nas variváveis de controle
  indoParaFrente = true;
  indoParaTras = false;

  // Aciona os motores para mover para frente
  analogWrite(3, velocidadeDireita);
  digitalWrite(4, LOW);
  digitalWrite(5, HIGH);

  analogWrite(6, velocidadeEsquerda);
  digitalWrite(7, LOW);
  digitalWrite(8, HIGH);
}

void andarParaDireita()
{
  // Faz o robô virar para a direita
  analogWrite(3, velocidadeDireita);
  digitalWrite(4, HIGH);
  digitalWrite(5, LOW);

  analogWrite(6, velocidadeEsquerda);
  digitalWrite(7, LOW);
  digitalWrite(8, HIGH);

  // Ajusta o tempo de virada com base no movimento atual
  if (indoParaFrente)
  {
    delay(45);
    moverParaFrente();
  }
  else if (indoParaTras)
  {
    delay(45);
    moverParaTras();
  }
  else
  {
    delay(190);
    parar();
  }
}

void andarParaEsquerda()
{
  // Faz o robô virar para a esquerda
  analogWrite(3, velocidadeDireita);
  digitalWrite(4, LOW);
  digitalWrite(5, HIGH);

  analogWrite(6, velocidadeEsquerda);
  digitalWrite(7, HIGH);
  digitalWrite(8, LOW);

  // Ajusta o tempo de virada com base no movimento atual
  if (indoParaFrente)
  {
    delay(45);
    moverParaFrente();
  }
  else if (indoParaTras)
  {
    delay(45);
    moverParaTras();
  }
  else
  {
    delay(190);
    parar();
  }
}

void moverParaTras()
{
  // Define a movimentação do robô para trás nas variváveis de controle
  indoParaFrente = false;
  indoParaTras = true;

  // Aciona os motores para mover para trás
  analogWrite(3, velocidadeDireita);
  digitalWrite(4, HIGH);
  digitalWrite(5, LOW);

  analogWrite(6, velocidadeEsquerda);
  digitalWrite(7, HIGH);
  digitalWrite(8, LOW);
}

void parar()
{
  // Para todos os movimentos do robô
  indoParaFrente = false;
  indoParaTras = false;

  // Desliga os motores
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(7, LOW);
  digitalWrite(8, LOW);
}
