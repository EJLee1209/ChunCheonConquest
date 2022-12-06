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
<img src="https://user-images.githubusercontent.com/101651909/205789463-dd531cba-e397-445c-a370-a59a0629f93b.png" width="40%"/> | <img src="https://user-images.githubusercontent.com/101651909/205787751-7041d314-be19-4fa8-b899-bf0fd2c1ca0f.png" width="40%"/>

회원가입/로그인 기능을 제공해 사용자의 데이터를 데이터베이스에 저장합니다.

### 2. Splash 화면
<img src="https://user-images.githubusercontent.com/101651909/205787888-82cef2af-443e-4f49-8c2a-39b6a53cafe3.png" width="40%"/>
로그인을 하면 Splash 화면이 나오고, 필요한 데이터를 데이터 베이스에서 가져온 후, 메인화면으로 넘어갑니다. <br>
Lottie 를 사용해서 로딩 애니메이션을 적용했습니다.

### 3. 메인 화면
<img src="https://user-images.githubusercontent.com/101651909/205787911-5fb12d2d-6f0b-48a7-89e5-116578276029.png" width="40%"/> | <img src="https://user-images.githubusercontent.com/101651909/205788064-c6b92eda-ab48-486f-804f-95f3eddd2a28.png" width="40%"/>
메인화면에서는 앱에 등록되어 있는 춘천의 장소들의 이미지와 이름들을 보여주고, 이미지를 클릭하면 상세 페이지를 보여줍니다. <br>
상세페이지에서는 해당 장소의 대표메뉴, 소개글, 전화번호, 네이버플레이스로 이동할 수 있는 링크, 지도상에서의 위치를 보여줍니다. <br>
또한, 춘천 정복을 얼마나 했는지 그래프와 수치로 확인할 수 있습니다. <br>

### 4. 게시물 등록
<img src="https://user-images.githubusercontent.com/101651909/205788094-e6d4029d-42c8-4d1a-a392-c3b98be0bdc7.png" width="30%"/> | <img src="https://user-images.githubusercontent.com/101651909/205788123-0d110da4-b1ec-4f34-872e-43ff6e7fb1df.png" width="30%"/> | <img src="https://user-images.githubusercontent.com/101651909/205788133-025ded6d-c02a-48e7-b50f-b1f18067c388.png" width="30%"/>

추억을 기록할 수 있는 게시물 기능입니다. <br>
이미 저장된 게시물을 터치하면 수정 또는 삭제가 가능합니다.

### 5. 네이버 지도 API 를 사용한 편의 기능

<figure class="third">
![image](https://user-images.githubusercontent.com/101651909/205788169-596083b7-8006-4ef2-b7c1-0833bdef0eb7.png)

![image](https://user-images.githubusercontent.com/101651909/205788197-58c04706-993e-4385-ae04-e6dfa3d2dc78.png)

![image](https://user-images.githubusercontent.com/101651909/205788227-dac99455-13d2-4e42-9ac9-f965fe2028da.png)
</figure>

앱에 등록되어 있는 장소들의 위치를 좀 더 정확하게 알 수 있는 지도 기능을 제공합니다. <br>
관광지, 카페, 식당 3가지의 카테고리로 분류되어있습니다. <br>
카테고리 버튼을 클릭하면 카테고리에 맞는 장소들을 지도상에 마커로 표시해줍니다. <br>
또한, 장소 검색기능도 제공합니다. <br>

<!-- 마커와 슬라이드를 연결해서 슬라이드를 할 때마다 해당 장소의 위치를 보여줍니다. -->

![c1](https://user-images.githubusercontent.com/101651909/205788318-be1c2023-0b52-40b9-a52c-99a6d82a2365.gif)
현재 사용자의 위치와 앱에 등록되어 있는 장소의 위치를 비교해서 10m 이내에 있다면, 해당 장소의 정복을 완료하고, 알림 메세지로 알려줍니다.

### 6. 프로필 변경

![image](https://user-images.githubusercontent.com/101651909/205788530-6e18cd11-5435-4a55-a029-6be7bc00c402.png)
이름과 프로필 사진을 변경할 수 있습니다.

### 7. 랭크시스템
![image](https://user-images.githubusercontent.com/101651909/205788556-99b1872b-1854-4379-89fe-1f80c62b7d3d.png)

# 🛠 개발환경 및 사용 라이브러리
- Android Studio(Kotlin)
- Visual Studio Code(Python)
- Firebase Realtime Database
- Firebase Authentication
- Firebase Storage
- Naver Map API

