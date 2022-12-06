# 📱 춘천 정복

## 📌 기획 의도
춘천에 대해 주변에서 들은 말들
- 춘천은 시골이다.
- 갈 곳이 없다.
- 닭갈비 말고 없다.
- 너무 춥다(?)
- 너무 덥다(?)

춘천에서 가볼만한 장소들을 모아 보여주고, 춘천에 대한 편견을 깰 수 있는 앱을 만들어 보자!

## 📌 주요 기능 및 인터페이스

### 1. 회원가입/로그인
<figure class="half">
<img src="https://velog.velcdn.com/images/dldmswo1209/post/7ab77152-4509-4549-9156-b57697c5a761/image.png" width="40%"/>
<img src="https://velog.velcdn.com/images/dldmswo1209/post/a93b424b-8a5d-40b3-b926-69ccf6a9235e/image.png" width="40%"/>
</figure>
회원가입/로그인 기능을 제공해 사용자의 데이터를 데이터베이스에 저장합니다.

### 2. Splash 화면
<img src="https://velog.velcdn.com/images/dldmswo1209/post/b38545a9-62dc-464e-aff7-ddd635017f40/image.png" width="40%"/>
로그인을 하면 Splash 화면이 나오고, 필요한 데이터를 데이터 베이스에서 가져온 후, 메인화면으로 넘어갑니다. <br>
Lottie 를 사용해서 로딩 애니메이션을 적용했습니다.

### 3. 메인 화면
<figure class="half">
  <img src="https://velog.velcdn.com/images/dldmswo1209/post/365cf974-fb7b-4ab7-83ba-6ee4f6d277a2/image.png" width="40%"/>
<img src="https://velog.velcdn.com/images/dldmswo1209/post/d565e6b7-5f65-4f31-b0a0-a779a75566d4/image.png" width="40%"/>
</figure>
메인화면에서는 앱에 등록되어 있는 춘천의 장소들의 이미지와 이름들을 보여주고, 이미지를 클릭하면 상세 페이지를 보여줍니다. <br>
상세페이지에서는 해당 장소의 대표메뉴, 소개글, 전화번호, 네이버플레이스로 이동할 수 있는 링크, 지도상에서의 위치를 보여줍니다. <br>
또한, 춘천 정복을 얼마나 했는지 그래프와 수치로 확인할 수 있습니다. <br>

### 4. 게시물 등록
<figure class="third">
    <img src="https://velog.velcdn.com/images/dldmswo1209/post/fc5eb1ab-af74-4cfe-9de6-3a75a5d85103/image.png" width="40%"/>
  <img src="https://velog.velcdn.com/images/dldmswo1209/post/fb1cf624-e6e8-4438-b227-a2802851921c/image.png" width="40%"/>
<img src="https://velog.velcdn.com/images/dldmswo1209/post/98140aeb-8f61-4b73-acaa-f21739ad37c0/image.png" width="40%"/>
</figure>

추억을 기록할 수 있는 게시물 기능입니다. <br>
이미 저장된 게시물을 터치하면 수정 또는 삭제가 가능합니다.

### 5. 네이버 지도 API 를 사용한 편의 기능

<figure class="third">
<img src="https://velog.velcdn.com/images/dldmswo1209/post/65d1ce3c-0841-405d-a80a-b1b7bd046a4d/image.png" width="40%"/>

<img src="https://velog.velcdn.com/images/dldmswo1209/post/142b3465-4997-46cb-939e-dbf7c962c38c/image.png" width="40%"/>

<img src="https://velog.velcdn.com/images/dldmswo1209/post/5401c4e5-0914-4f91-843b-4ebc14c83d48/image.png" width="40%"/>
</figure>

앱에 등록되어 있는 장소들의 위치를 좀 더 정확하게 알 수 있는 지도 기능을 제공합니다. <br>
관광지, 카페, 식당 3가지의 카테고리로 분류되어있습니다. <br>
카테고리 버튼을 클릭하면 카테고리에 맞는 장소들을 지도상에 마커로 표시해줍니다. <br>
또한, 장소 검색기능도 제공합니다. <br>

<img src="https://velog.velcdn.com/images/dldmswo1209/post/63e222f9-0493-489b-b672-1897ee98345b/image.gif" width="40%"/>
마커와 슬라이드를 연결해서 슬라이드를 할 때마다 해당 장소의 위치를 보여줍니다.

<img src="https://velog.velcdn.com/images/dldmswo1209/post/79a0f162-380a-4f01-90c5-61ccd37391df/image.gif" width="40%"/>
현재 사용자의 위치와 앱에 등록되어 있는 장소의 위치를 비교해서 10m 이내에 있다면, 해당 장소의 정복을 완료하고, 알림 메세지로 알려줍니다.

### 6. 프로필 변경

<img src="https://velog.velcdn.com/images/dldmswo1209/post/46edc75a-6035-41b0-8c45-4d5e08e00ee6/image.png" width="40%"/>
이름과 프로필 사진을 변경할 수 있습니다.

### 7. 랭크시스템
<img src="https://velog.velcdn.com/images/dldmswo1209/post/8b9c4ace-036f-484d-ba16-ba3bfff08e82/image.png" width="40%"/>

# 🛠 개발환경 및 사용 라이브러리
- Android Studio(Kotlin)
- Visual Studio Code(Python)
- Firebase Realtime Database
- Firebase Authentication
- Firebase Storage
- Naver Map API

