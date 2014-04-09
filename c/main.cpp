//
//  main.cpp
//  StringTable
//
//  Created by admin on 14-4-9.
//  Copyright (c) 2014年 redparrots. All rights reserved.
//

/*
 * 按'\n'生成字符串表
 */

#include <iostream>
#include <string>
#include <vector>

using namespace std;

int main(int argc, const char * argv[])
{
    string exedir = argv[0];
    int index = (int)exedir.find_last_of('/');
    string workdir = "";
    
    if (index != -1)
    {
        workdir = exedir.substr(0,index + 1);
    }
    
    string file = workdir + "strings.txt";
    FILE* pFile = fopen(file.c_str(), "rb");
    if (pFile)
    {
        printf("open %s success.\n",file.c_str());
        fseek(pFile, 0, SEEK_END);
        long size = ftell(pFile);
        fseek(pFile, 0, SEEK_SET);
        
        char* buf = new char[size + 1];
        long readed = fread(buf, sizeof(char), size, pFile);
        if (readed != size)
        {
            printf("read %s failed.\n",file.c_str());
        }
        string temp = buf;
        delete buf;
        
        size_t pos = -1;
        vector<string> stringTable;
        
        while ( (pos = temp.find_first_of('\n')) != -1)
        {
            string item = temp.substr(0,pos);
            
            stringTable.push_back(item);
            
            temp = temp.substr(pos + 1);
        }
        fclose(pFile);
        
        for (int i = 0; i < stringTable.size(); i++)
        {
            printf("%d:%s\n",i,stringTable.at(i).c_str());
        }
    }
    
    return 0;
}

