/**

    StanfordBunnyModel
    
    @update     2016/06/22
    @develop    K.Asai ,
    
*/

package stanfordbunny;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class StanfordBunnyModel extends Object{
    
    // 角度情報
    private float[] degree  = {0.0f,0.0f,0.0f};
    // 角度フラグ
    private Integer prevX   = -1;
    private Integer prevY   = -1;
    
    private static final String FilePath = "resource/StanfordBunny.ply"; 
    private PlyData[] points = new PlyData[35947];
    private int[][] face = new int[69451][4];
    private int prNum = 0;
    private int j = 0; 
    private int k = 0;
    
    public StanfordBunnyModel(){
        this.fileRead(FilePath);
    }
    
    public PlyData[] getPoints(){
        return points;
    }
    
    public int [][] getFace(){
        return face;
    }
    
    
    private void fileRead(String filePath) {
        FileReader      fr = null;
        BufferedReader  br = null;
        try {
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                String[] pts = line.split(" ",0);
                System.out.println(pts[0]);
                prNum++;
                if(pts[0].equals("end_header")){
                        break;
                }
                
            }
            
            while ((line = br.readLine()) != null) {
                String[] pts = line.split(" ",0);
                if(pts.length == 5){
                        points[j] = new PlyData(Double.parseDouble(pts[0]),Double.parseDouble(pts[1]),Double.parseDouble(pts[2]),Double.parseDouble(pts[3]),Double.parseDouble(pts[4]));
                    j++;
                }else if(pts.length == 4){
                    for(int i=0;i<pts.length;i++){
                        if(i == 0){
                            face[k][i] = Integer.parseInt(pts[i]); 
                        }else{
                            face[k][i] = Integer.parseInt(pts[i])-prNum;
                        }
                    }
                    k++;
                }
                
            }
            System.out.println("points size = " + points.length );
            System.out.println("face size = " + face.length );
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    
    // --------------------------------------
    // 以下、M-V-C連携時生成
    
    public void setDegree(float x,float y,float z){
        this.degree[0] += x;
        this.degree[1] += y;
        this.degree[2] += z;
    }
    
    public float[] getDegree(){ return this.degree; }
    
    /**
        角度情報のフラグをリセットする(詳細未確認)
    */
    public void resetPrev(){
        this.prevX = -1;
        this.prevY = -1;
    }
    
    /**
        角度情報を更新する
        @param x
        @param y
        @return degree(角度情報)を返す
    */
    public float[] rotation(Integer x, Integer y){
                
        if(!prevX.equals(-1) || !prevY.equals(-1)){
            Integer distanceX = (x - this.prevX) / 2;
            Integer distanceY = (y - this.prevY) / 2;
            
            this.setDegree(distanceY.floatValue(),distanceX.floatValue(),0);
        }       
        
        this.prevX = x;
        this.prevY = y;
        return this.getDegree();
    }
    

    /**
        角度情報をX方向に更新する
        @return degree(角度情報)を返す
    */
    public float[] rotaX(){
        this.setDegree(1,0,0);
        return this.getDegree();
    }
    
    /**
        角度情報をY方向に更新する
        @return degree(角度情報)を返す
    */
    public float[] rotaY(){
        this.setDegree(0,1,0);
        return this.getDegree();
    }
    
    /**
        角度情報をY方向に更新する
        @return degree(角度情報)を返す
    */
    public float[] rotaZ(){
        this.setDegree(0,0,1);
        return this.getDegree();
    }
    
    
}