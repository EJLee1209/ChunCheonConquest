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

<img src="https://user-images.githubusercontent.com/101651909/206101777-bdb3accf-ba30-4e33-bf66-484e24a5e0d6.png" width="40%"/> | <img src="https://user-images.githubusercontent.com/101651909/206101831-55f4fe44-96f7-44e3-8cca-5b670ba82a02.png" width="40%"/>

회원가입/로그인 기능을 제공해 사용자의 데이터를 데이터베이스에 저장합니다.

### 2. Splash 화면
<img src="https://user-images.githubusercontent.com/101651909/206102001-5bd5b04b-788d-40df-84d9-355a33b4c074.png" width="40%"/>


로그인을 하면 Splash 화면이 나오고, 필요한 데이터를 데이터 베이스에서 가져온 후, 메인화면으로 넘어갑니다. <br>
Lottie 를 사용해서 로딩 애니메이션을 적용했습니다.

### 3. 메인 화면
<img src="https://user-images.githubusercontent.com/101651909/206102064-c6cafcdc-2917-49a0-8239-b0bb3092d103.png" width="40%"/> | <img src="https://user-images.githubusercontent.com/101651909/206102150-0c3cb829-a3d2-4fb8-923c-9c4db52669ea.png" width="40%"/>

메인화면에서는 앱에 등록되어 있는 춘천의 장소들의 이미지와 이름들을 보여주고, 이미지를 클릭하면 상세 페이지를 보여줍니다. <br>
상세페이지에서는 해당 장소의 대표메뉴, 소개글, 전화번호, 네이버플레이스로 이동할 수 있는 링크, 지도상에서의 위치를 보여줍니다. <br>
또한, 춘천 정복을 얼마나 했는지 그래프와 수치로 확인할 수 있습니다. <br>

### 4. 게시물 등록
<img src="https://user-images.githubusercontent.com/101651909/206102245-8cc760e4-30ff-4f92-849a-5e0b6e73f7b0.png" width="30%"/> | <img src="https://user-images.githubusercontent.com/101651909/206102274-f7fb89d6-e0da-4ea5-a5e3-01ea789b6cb9.png" width="30%"/> | <img src="https://user-images.githubusercontent.com/101651909/206102278-b12ffc3c-f827-4457-8eb7-01451145decd.png" width="30%"/>

추억을 기록할 수 있는 게시물 기능입니다. <br>
이미 저장된 게시물을 터치하면 수정 또는 삭제가 가능합니다.

### 5. 네이버 지도 API 를 사용한 편의 기능
<img src="https://user-images.githubusercontent.com/101651909/206102425-e03e56e2-56a8-4341-8f10-afb4bca2459c.png" width="40%"/> | <img src="https://user-images.githubusercontent.com/101651909/206102433-ce053d46-0e73-40d1-996f-412d2932394b.png" width="40%"/> | <img src="https://user-images.githubusercontent.com/101651909/206102437-2409698f-c533-46ea-b38c-796f7b0faaef.png" width="40%"/>

앱에 등록되어 있는 장소들의 위치를 좀 더 정확하게 알 수 있는 지도 기능을 제공합니다. <br>
관광지, 카페, 식당 3가지의 카테고리로 분류되어있습니다. <br>
카테고리 버튼을 클릭하면 카테고리에 맞는 장소들을 지도상에 마커로 표시해줍니다. <br>
또한, 장소 검색기능도 제공합니다. <br>

<!-- 마커와 슬라이드를 연결해서 슬라이드를 할 때마다 해당 장소의 위치를 보여줍니다. -->

<img src="https://user-images.githubusercontent.com/101651909/205788318-be1c2023-0b52-40b9-a52c-99a6d82a2365.gif" width="40%"/>

현재 사용자의 위치와 앱에 등록되어 있는 장소의 위치를 비교해서 10m 이내에 있다면, 해당 장소의 정복을 완료하고, 알림 메세지로 알려줍니다.

### 6. 프로필 변경
<img src="https://user-images.githubusercontent.com/101651909/206102541-0b8f4543-5773-4b3f-bf9e-54c8c2098878.png" width="40%"/>

이름과 프로필 사진을 변경할 수 있습니다.

### 7. 랭크시스템
<img src="https://user-images.githubusercontent.com/101651909/206102547-057b33d2-d63e-4afe-b3a4-111cc498157e.png" width="40%"/>


# 🛠 개발환경 및 사용 라이브러리
- Android Studio(Kotlin)
- Visual Studio Code(Python)
- Firebase Realtime Database
- Firebase Authentication
- Firebase Storage
- Naver Map API

