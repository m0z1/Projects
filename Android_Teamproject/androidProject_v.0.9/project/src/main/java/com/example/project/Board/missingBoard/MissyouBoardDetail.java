package com.example.project.Board.missingBoard;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project.Board.BoardClient;
import com.example.project.Board.BoardInterface;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.comment.CommentClient;
import com.example.project.comment.CommentService;
import com.example.project.databinding.ActivityMissyouBoardDetailBinding;
import com.example.project.member.Member;
import com.example.project.member.MemberClient;
import com.example.project.member.MemberService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissyouBoardDetail extends AppCompatActivity {

    private ActivityMissyouBoardDetailBinding binding;

    List<MissingBoard> missingBoardList;

    TextView txusername, txtel, txname;

    MissyouAdapter missyouAdapter;

    String tel;

    String basUrl = "http://10.100.102.45:8899";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missyou_board_detail);

        binding = ActivityMissyouBoardDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        missyouAdapter = new MissyouAdapter(missingBoardList);


        //이미지 클릭 -> 홈으로
        binding.missyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //뒤로가기
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //이미지 받아오기

        //Intent로 missingId값 받아오기
        Intent intentCmt = getIntent();
        Long missingId = intentCmt.getLongExtra("missingId", 0);
        Log.i("intent 값", missingId+"");

        //상세 내용 받아오기
        BoardInterface boardInterface = BoardClient.getInstance().getBoardInterface();
        Call<MissingBoard> call1 = boardInterface.view2(missingId);
        call1.enqueue(new Callback<MissingBoard>() {
            @Override
            public void onResponse(Call<MissingBoard> call, Response<MissingBoard> response) {
                MissingBoard missingBoard = response.body();

                binding.txfinderId.setText(1+"");
                //발견자에 유저네임 뿌리기 ----> 다시해야 함
                MemberService memberService = MemberClient.getInstance().getMemberService();
                Call<Member> call2 = memberService.findById(Long.parseLong(binding.txfinderId.getText().toString()));    //멤버 아이디 줘야함 (글 작성할 때, member 객체에 아이디 넣어주기)
                call2.enqueue(new Callback<Member>() {
                    @Override
                    public void onResponse(Call<Member> call, Response<Member> response) {
                        Member member = response.body();
                        binding.txfinderId.setText(member.getUsername());
                        //findId에 스타일 적용
                        binding.txfinderId.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                    }

                    @Override
                    public void onFailure(Call<Member> call, Throwable t) {

                    }
                });

                //발견자에 dialogView 띄우기
                binding.txfinderId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View diaglogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.member_dialog,null);
                        AlertDialog.Builder dlg = new AlertDialog.Builder(view.getContext());
                        dlg.setTitle("연락처 상세보기");
                        dlg.setView(diaglogView);

                        //finderId값을 사용해서 member 정보 가져와서 뿌리기 ---> 이것도 다시하기
                        MemberService memberService = MemberClient.getInstance().getMemberService();
                        //Call<Member> call1 = memberService.findById(Long.parseLong(binding.txfinderId.getText().toString()));
                        Call<Member> call1 = memberService.findById(Long.parseLong(1+""));  //MissingBoard의 memberId 받아와서 줘야함 (글 작성할 때 member객체에 입력)

                        call1.enqueue(new Callback<Member>() {
                            @Override
                            public void onResponse(Call<Member> call, Response<Member> response) {
                                Member member = response.body();
                                txusername = diaglogView.findViewById(R.id.txusername);
                                txtel = diaglogView.findViewById(R.id.txtel);
                                txname = diaglogView.findViewById(R.id.txname);


                                txname.setText(member.getName());
                                txusername.setText(member.getUsername());
                                txtel.setText(member.getTel());
                                tel = member.getTel();
                            }

                            @Override
                            public void onFailure(Call<Member> call, Throwable t) {

                            }
                        });

                        dlg.setPositiveButton("연락하기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(tel != null){
                                    Uri uri = Uri.parse("tel:"+tel);
                                    Log.i("다이얼 번호:", ""+uri);
                                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                                    view.getContext().startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "연락처가 등록되어있지 않습니다. 댓글을 남겨보세요!", Toast.LENGTH_LONG).show();
                                    dialogInterface.cancel();
                                }

                            }
                        });
                        dlg.setNegativeButton("취소", null);
                        dlg.show();
                    }
                });

                binding.txbreed.setText(missingBoard.getBreed());
                binding.txpetgender.setText(missingBoard.getPetgender());
                binding.txpetcharacter.setText(missingBoard.getPetcharacter());
                binding.txfindAddr.setText(missingBoard.getMissingaddr());
                binding.txPetBreedTitle.setText(missingBoard.getBreed());
                binding.txcontent.setText(missingBoard.getContent());
                binding.txpetage.setText(missingBoard.getPetage());
                binding.txPetName.setText(missingBoard.getPetname());

                //날짜 포맷 변경
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = simpleDateFormat.format(missingBoard.getRegdate());

                binding.txregdate.setText(date);

                //이미지 받아오기
                Glide.with(getApplicationContext())
                        .load(basUrl+response.body().getImgFileList().get(0).getImgUrl())
                        .override(500,400)
                        .into(binding.img1);
                Glide.with(getApplicationContext())
                        .load(basUrl+response.body().getImgFileList().get(1).getImgUrl())
                        .override(500,400)
                        .into(binding.img2);
                Glide.with(getApplicationContext())
                        .load(basUrl+response.body().getImgFileList().get(2).getImgUrl())
                        .override(500,400)
                        .into(binding.img3);
            }

            @Override
            public void onFailure(Call<MissingBoard> call, Throwable t) {

            }
        });

        //화면 시작 시 댓글 수 불러오기
        //댓글 개수 가져오기
        CommentService commentService1 = CommentClient.getInstance().getCommentService();
        Call<Integer> callcmt = commentService1.count2(missingId);
        callcmt.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                binding.btnCmt.setText("댓글("+response.body()+")");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });


        //Intent Launcher
        //1. 댓글창에서 돌아갈 때 댓글 수 늘려주기
        //2. 수정 후 내용 뿌려주기
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent intent = result.getData();
                        if(result.getResultCode()== Activity.RESULT_OK){
                            Integer cmtcnt;
                            cmtcnt = intent.getIntExtra("cmtcnt", 0);
                            binding.btnCmt.setText("댓글("+cmtcnt+")");
                            return;
                        } else if(result.getResultCode()==Activity.RESULT_FIRST_USER) {
                            MissingBoard missingBoard = (MissingBoard) intent.getSerializableExtra("missingBoard");
                            binding.txfindAddr.setText(missingBoard.getMissingaddr());
                            binding.txcontent.setText(missingBoard.getContent());
                            binding.txbreed.setText(missingBoard.getBreed());
                            binding.txpetgender.setText(missingBoard.getPetgender());
                            binding.txpetage.setText(missingBoard.getPetage());
                            binding.txpetcharacter.setText(missingBoard.getPetcharacter());
                            binding.txPetName.setText(missingBoard.getPetname());
                            binding.txPetBreedTitle.setText(missingBoard.getBreed());
                        }
                    }
                });



        //댓글창 이동 (Intent)
        binding.btnCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCmt = new Intent(getApplicationContext(), MissyouCommentList.class);
                intentCmt.putExtra("missingId", missingId);
                launcher.launch(intentCmt);
            }
        });


        //채팅창 이동




        //로그인 정보와 작성자가 같다면 아래 버튼 생성
        //게시글 수정
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUpdate = new Intent(getApplicationContext(), MissyouBoardUpdate.class);
                intentUpdate.putExtra("missingId", missingId);
                launcher.launch(intentUpdate);
            }
        });


        //게시글 삭제
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MissyouBoardDetail.this);
                dlg.setTitle("정말 삭제하시겠습니까?");
                dlg.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BoardInterface boardInterface1 = BoardClient.getInstance().getBoardInterface();
                        Call<Void> call = boardInterface1.deleteById2(missingId);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(getApplicationContext(), "삭제 완료!", Toast.LENGTH_SHORT).show();
                                missyouAdapter.deleteItem(missingId);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                });
                dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();   //다이얼로그 창 닫기
                    }
                });
                dlg.show();
            }
        });
    }
}