<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>

 <!DOCTYPE html>
 <html lang="en">
 <head>
     <meta charset="UTF-8">
     <meta http-equiv="X-UA-Compatible" content="IE=edge">
     <meta name="viewport" content="width=device-width, initial-scale=1.0">
     <title>KH 커뮤니티</title>
 
     <link rel="stylesheet" href="../resources/css/main-style.css">
     
     <link rel="stylesheet" href="../resources/css/signUp-style.css">
 
     <script src="https://kit.fontawesome.com/a2e8ca0ae3.js" crossorigin="anonymous"></script>
 </head>
 <body>
     <main>
        <!-- hedaer include -->
        <jsp:include page="/WEB-INF/views/common/header.jsp" />


         <!-- 회원가입  -->
         <section class="signUp-content">
 
            <!-- 회원가입 화면 전환 주소(GET)와 같은 주소로 
                실제 회원가입을 요청(POST)
                -> 요청 주소가 같아도 데이터 전달 방식이 다르면 중복 허용
            -->

            <!-- 
                절대경로 : /community/member/signUp
                상대경로 : signUp
             -->

            <form action="signUp" method="POST" name="signUp-form" onsubmit="return signUpValidate()">
 
                 <label for="memberEmail">
                     <span class="required">*</span> 아이디(이메일)
                 </label>
                 
                 <div class="signUp-input-area">
                     <input type="text" id="memberEmail" name="memberEmail"
                             placeholder="아이디(이메일)" maxlength="30"
                             autocomplete="off" required>
 
                     <!-- autocomplete="off" : 자동완성 미사용 -->
                     <!-- required : 필수 작성 input 태그 -->
                     
                     <!-- 자바스크립로 코드 추가 예정 -->
                     <button type="button" id="sendBtn">인증번호 받기</button>
                 </div>
 
                 <span class="signUp-message" id="emailMessage">메일을 받을 수 있는 이메일을 입력해주세요.</span>
 
 
 
                 <label for="emailCheck">
                     <span class="required">*</span> 인증번호
                 </label>
                 
                 <div class="signUp-input-area">
                     <!-- cNumber -->
                     <input type="text" id="cNumber"  
                             placeholder="인증번호 입력" maxlength="6"
                             autocomplete="off">
 
                     <button type="button" id="cBtn">인증하기</button>
                 </div>
 
                 <!-- 5:00 타이머 / 인증되었습니다(녹색) / 인증 시간이 만료되었습니다.(빨간색) -->
                 <span class="signUp-message" id="cMessage" ></span>
 
 
 
 
                 <label for="memberPw">
                     <span class="required">*</span> 비밀번호
                 </label>
                 
                 <div class="signUp-input-area">
                     <input type="text" id="memberPw" name="memberPw"
                             placeholder="비밀번호" maxlength="30">
                 </div>
 
                 <div class="signUp-input-area">
                     <input type="text" id="memberPwConfirm"
                             placeholder="비밀번호 확인" maxlength="30">
                 </div>
 
                 <span class="signUp-message" id="pwMessage">영어, 숫자, 특수문자(!,@,#,-,_) 6~30글자 사이로 작성해주세요.</span>
 
 
 
 
                 <label for="memberNickname">
                     <span class="required">*</span> 닉네임
                 </label>
                 
                 <div class="signUp-input-area">
                     <input type="text" id="memberNickname" name="memberNickname"
                             placeholder="닉네임" maxlength="10">
                 </div>
 
                 <span class="signUp-message" id="nicknameMessage">영어/숫자/한글 2~10글자 사이로 작성해주세요.</span>
 
 
 
                 <label for="memberTel">
                     <span class="required">*</span> 전화번호
                 </label>
                 
                 <div class="signUp-input-area">
                     <input type="text" id="memberTel" name="memberTel"
                             placeholder="(- 없이 숫자만 입력)" maxlength="11">
                 </div>
 
                 <span class="signUp-message" id="telMessage">전화번호를 입력해주세요.(- 제외)</span>
              
              
              
                 <label for="memberAddress">
                     주소
                 </label>
                 
                 <div class="signUp-input-area">
                     <input type="text" id="sample6_postcode" name="memberAddress"
                             placeholder="우편번호">
                     <button type="button" onclick="sample6_execDaumPostcode()">검색</button>
                 </div>
 
                 <div class="signUp-input-area">
                     <input type="text" id="sample6_address" name="memberAddress" placeholder="도로명주소">
                 </div>
 
                 <div class="signUp-input-area">
                     <input type="text" id="sample6_detailAddress" name="memberAddress" placeholder="상세주소">
                 </div>
                 <!-- <div class="signUp-input-area">
                     <input type="text" id="sample3_extraAddress" name="memberAddress" placeholder="참고항목">
                 </div> -->
 
                 <button type="submit" id="signUp-btn">가입하기</button>
 
             </form>
             
         </section>
 
 
     </main>
 
    <!-- footer include -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />


    <!-- jQuery 라이브러리 추가(CDN) -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>

    <!-- signUp.js 연결 -->
    <script src="${contextPath}/resources/js/member/signUp.js"></script>

    <!-- kakao 주소 api -->
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <input type="text" id="sample6_postcode" placeholder="우편번호">
    <input type="button" onclick="sample6_execDaumPostcode()" value="우편번호 찾기"><br>
    <input type="text" id="sample6_address" placeholder="주소"><br>
    <input type="text" id="sample6_detailAddress" placeholder="상세주소">
    <input type="text" id="sample6_extraAddress" placeholder="참고항목">
    
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script>
        function sample6_execDaumPostcode() {
            new daum.Postcode({
                oncomplete: function(data) {
                    // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
    
                    // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                    // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                    var addr = ''; // 주소 변수
                    var extraAddr = ''; // 참고항목 변수
    
                    //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                    if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                        addr = data.roadAddress;
                    } else { // 사용자가 지번 주소를 선택했을 경우(J)
                        addr = data.jibunAddress;
                    }
    
                    // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                    if(data.userSelectedType === 'R'){
                        // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                        // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                        if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                            extraAddr += data.bname;
                        }
                        // 건물명이 있고, 공동주택일 경우 추가한다.
                        if(data.buildingName !== '' && data.apartment === 'Y'){
                            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                        }
                        // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                        if(extraAddr !== ''){
                            extraAddr = ' (' + extraAddr + ')';
                        }
                        // 조합된 참고항목을 해당 필드에 넣는다.
                        document.getElementById("sample6_extraAddress").value = extraAddr;
                    
                    } else {
                        document.getElementById("sample6_extraAddress").value = '';
                    }
    
                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                    document.getElementById('sample6_postcode').value = data.zonecode;
                    document.getElementById("sample6_address").value = addr;
                    // 커서를 상세주소 필드로 이동한다.
                    document.getElementById("sample6_detailAddress").focus();
                }
            }).open();
        }
    </script>

 </body>
 </html>