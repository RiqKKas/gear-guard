#include <SoftwareSerial.h>
#include <Ultrasonic.h>

SoftwareSerial mySerial(11, 12); // RX, TX
String command = ""; 
bool indoParaFrente = false;
bool indoParaTras = false;

Ultrasonic ultrasonic(10, 9);
long distancia = 0;
int velocidadeDireita = 70;
int velocidadeEsquerda = 95;

void setup() {
  pinMode(3, OUTPUT);
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
  pinMode(6, OUTPUT);
  pinMode(7, OUTPUT);
  pinMode(8, OUTPUT);

  Serial.begin(115200);
  mySerial.begin(9600);
}

void loop() {
  if (mySerial.available()) {
    command = mySerial.readStringUntil('\n');
    interpretarComandoBluetooth(command);
  }

  distancia = ultrasonic.read(CM);
  ajustarVelocidade();
}

void ajustarVelocidade() {
  if (!indoParaTras) {
    if (distancia < 20) {
      parar();
    } else if (distancia < 80) {
      setVelocidade(70, 95);
    } else {
      setVelocidade(89, 126);
    }
  }
}

void setVelocidade(int velocidadeDireitaAux, int velocidadeEsquerdaAux) {
  velocidadeDireita = velocidadeDireitaAux;
  velocidadeEsquerda = velocidadeEsquerdaAux;
  
  analogWrite(3, velocidadeDireita);
  analogWrite(6, velocidadeEsquerda);
}

void interpretarComandoBluetooth(String cmd) {
  if (cmd == "frente") {
    moverParaFrente();
  } else if (cmd == "direita") {
    andarParaDireita();
  } else if (cmd == "esquerda") {
    andarParaEsquerda();
  } else if (cmd == "tras") {
    moverParaTras();
  } else if (cmd == "parar") {
    parar();
  }
}

void moverParaFrente() {
  indoParaFrente = true;
  indoParaTras = false;

  analogWrite(3, velocidadeDireita);
  digitalWrite(4, LOW);
  digitalWrite(5, HIGH);

  analogWrite(6, velocidadeEsquerda);
  digitalWrite(7, LOW);
  digitalWrite(8, HIGH);
}

void andarParaDireita() {
  analogWrite(3, velocidadeDireita);
  digitalWrite(4, HIGH);
  digitalWrite(5, LOW);

  analogWrite(6, velocidadeEsquerda);
  digitalWrite(7, LOW);
  digitalWrite(8, HIGH);

  if (indoParaFrente) {
    delay(45);
    moverParaFrente();
  } else if (indoParaTras) {
    delay(45);
    moverParaTras();
  } else {
    delay(190);
    parar();
  }

}

void andarParaEsquerda() {
  analogWrite(3, velocidadeDireita);
  digitalWrite(4, LOW);
  digitalWrite(5, HIGH);

  analogWrite(6, velocidadeEsquerda);
  digitalWrite(7, HIGH);
  digitalWrite(8, LOW);

  if (indoParaFrente) {
    delay(45);
    moverParaFrente();
  } else if (indoParaTras) {
    delay(45);
    moverParaTras();
  } else {
    delay(190);
    parar();
  }

}

void moverParaTras() {
  indoParaFrente = false;
  indoParaTras = true;

  analogWrite(3, velocidadeDireita);
  digitalWrite(4, HIGH);
  digitalWrite(5, LOW);

  analogWrite(6, velocidadeEsquerda);
  digitalWrite(7, HIGH);
  digitalWrite(8, LOW);
}

void parar() {
  indoParaFrente = false;
  indoParaTras = false;

  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(7, LOW);
  digitalWrite(8, LOW);
}
