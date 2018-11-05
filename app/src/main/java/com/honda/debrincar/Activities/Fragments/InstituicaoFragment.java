package com.honda.debrincar.Activities.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.honda.debrincar.Config.ConfiguraçaoFirebase;
import com.honda.debrincar.Objetos.Usuario;
import com.honda.debrincar.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstituicaoFragment extends Fragment {


    private Usuario usuario = new Usuario();

    private EditText nome;
    private EditText cnpj;
    private EditText descricao;
    private EditText endereco;
    private EditText cep;

    private EditText email;
    private EditText senha;
    private EditText confSenha;

    private Uri imagemUsuario;

    private StorageReference ImagemContaUsuarioRef;

    private ProgressBar progressBar;


    public InstituicaoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_instituicao, container, false);

        //Define ser instituição
        usuario.setPessoaFisica(false);

        //CARREGAR IMAGEM
        CircleImageView userImage = view.findViewById(R.id.set_imagem_inst);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVersaoAndroid();
            }
        });

        //Funções de cadastro quando o botão de cadastrar é pressionado
        Button btnCadastrar = view.findViewById(R.id.btn_cadastro_inst);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //SALVA OS CAMPOS EM VARIÁVEIS.
                nome = getActivity().findViewById(R.id.cad_nome_inst);
                cnpj = getActivity().findViewById(R.id.cad_cnpj_inst);
                descricao = getActivity().findViewById(R.id.cad_descricao_inst);
                endereco = getActivity().findViewById(R.id.cad_endereco_inst);
                cep = getActivity().findViewById(R.id.cad_cep_inst);


                //CAMPOS PARA LOGIN
                email = getActivity().findViewById(R.id.cad_email_inst);
                senha = getActivity().findViewById(R.id.cad_senha_inst);
                confSenha = getActivity().findViewById(R.id.cad_conf_senha_inst);

                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                String confirmaSenha = confSenha.getText().toString();
                if (usuario.getSenha().equals(confirmaSenha)){

                    //SETA OS DADOS DO USUÁRIO.
                    usuario.setNome(nome.getText().toString());
                    usuario.setCnpj(cnpj.getText().toString());
                    usuario.setDescricao(descricao.getText().toString());
                    usuario.setEndereco(endereco.getText().toString());
                    usuario.setCep(cep.getText().toString());

                    //FUNÇÃO DE CADASTRO DO USUÁRIO NO FIREBASE.
                    cadastraUsuario(usuario.getEmail(), usuario.getSenha());
                }else {
                    Toast.makeText(getActivity(),
                            "Confirmação da senha não confere!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void checkVersaoAndroid(){
        //PEDE PERMISSÃO SE A VERSÃO DO ANDROID FOR MENOR QUE A M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);
            }catch (Exception e){

            }
        } else {
            pickImage();
        }

    }

    //MÉTODO PARA PEGAR A IMAGEM
    private void pickImage() {

        CropImage.startPickImageActivity(getContext(), this);

    }

    //MÉTODO PARA REQUERIR O CORTE DA IMAGEM
    private void croprequest(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .setMultiTouchEnabled(true)
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //RESULT FROM SELECTED IMAGE
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);
            croprequest(imageUri);
        }

        //RESULT FROM CROPING ACTIVITY
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getUri());
                    imagemUsuario = result.getUri();
                    //Coloca a imagem no fragmento de cadastro, visivel ao usuário
                    ((ImageView)getActivity().findViewById(R.id.set_imagem)).setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 555 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            checkVersaoAndroid();
        }
    }


    //FUNÇÃO DE CADASTRO DO USUÁRIO NO FIREBASE
    public void cadastraUsuario(String email, String senha){

        progressBar = getActivity().findViewById(R.id.pb_instituicao);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        //Cria um novo usuário no Firebase
        Task<AuthResult> tarefa = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha);
        tarefa.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    //Resgata o ID do usuário cadastrado.
                    final FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(usuarioFirebase.getUid());
                    usuario.salvarDados();//Função para salvar dados do usuário no Firebase

                    if (imagemUsuario != null) {
                        //Salva imagem do usuário no FireStorage
                        ImagemContaUsuarioRef = ConfiguraçaoFirebase.getFirebaseStorage().child("Imagens Usuário");
                        StorageReference pastaStorage = ImagemContaUsuarioRef.child(usuario.getId() + ".jpg");
                        UploadTask uploadTask = pastaStorage.putFile(imagemUsuario);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Imagem do usuário salva com sucesso!", Toast.LENGTH_LONG).show();
                                    Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {

                                            if (task.isSuccessful()) {
                                                usuario.setImagemUsuarioUrl(task.getResult().toString());
                                                usuario.salvarDados(usuario.getImagemUsuarioUrl());

                                                Toast.makeText(getActivity(), "Url salva com sucesso!", Toast.LENGTH_LONG).show();

                                                progressBar.setVisibility(View.GONE);

                                                Intent intent = new Intent("TELA_ANUNCIOS_ACT");
                                                intent.addCategory("TELA_ANUNCIOS_CTG");
                                                startActivity(intent);
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getActivity(), "Falha em salvar a Url da imagem", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {

                        progressBar.setVisibility(View.GONE);
                        
                        Intent intent = new Intent("TELA_ANUNCIOS_ACT");
                        intent.addCategory("TELA_ANUNCIOS_CTG");
                        startActivity(intent);
                    }

                } else {
                    //Apresenta mensagem em casos de erro no cadastro.
                    Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
