# Dolbomi

돌보미는 유치원과 학부모 간 커뮤니케이션 앱 입니다.

교사와 학부모 간 채팅, 게시판을 통한 정보 공유, 출석부 등하원 푸시알림, 학원차량의 위치 등을 실시간으로 제공하는 앱 입니다.

 ![돌보미 커버_2](https://user-images.githubusercontent.com/55440637/159394548-6db7dda5-5254-4467-925e-13ac75b92a6a.png)
<img src="https://user-images.githubusercontent.com/55440637/159394033-092194a3-89e7-4894-88d1-f7f598a7c4a9.png" width="280" height="560"/>
<img src="https://user-images.githubusercontent.com/55440637/159394039-4a44ef0f-5e98-43b8-9f89-418d70b91736.png" width="280" height="560"/>
<img src="https://user-images.githubusercontent.com/55440637/159394043-ac664565-5dbc-4cc6-a432-a33ac4a26485.png" width="280" height="560"/>
<img src="https://user-images.githubusercontent.com/55440637/159394046-2a556df2-c7f8-4f1e-bd2a-9732f0bf1f26.png" width="280" height="560"/>
<img src="https://user-images.githubusercontent.com/55440637/159394052-f1381e32-f3d8-47ca-9741-f56e15ed36cb.png" width="280" height="560"/>
<img src="https://user-images.githubusercontent.com/55440637/159394055-d82a5f5a-f014-462b-93a9-c6841074a148.png" width="280" height="560"/>

## 🛠️ 사용 기술 및 라이브러리

- Java, AOS
- Firebase Realtime DB, Firebase Cloud Messaging
- Gilde, ~~ButterKnife~~, T Map API

## 📱 담당한 기능 (AOS)

- **로그인, 회원가입 사용자 관리**
- **메인 화면, 공지사항, 알림장, 식단표, 앨범** UI
- **차량위치** api연동
- **등하원 푸시알림 기능**

## 💡 깨달은 점

- **FirebaseAuth** 사용자 관리
- **Firebase RealtimeDB**, 실시간 정보와 사진을 보여주는 화면 구현 경험
- **Firebase Cloud Messaging,** 회원가입 고유 토큰값 사용해 push알림 전송
- **RecyclerView로 MultiDepth 리스트** 구현 경험, ListView는 재사용성이 떨어지기 때문에 RecyclerView를 사용하면 ViewHolder패턴을 이용하여 View의 재사용이 가능
- ~~**ButterKnife 라이브러리**를 사용해 View Inject 쉽게 할 수 있음~~
