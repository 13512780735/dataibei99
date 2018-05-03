package com.likeit.currenciesapp.im;

import android.util.Log;

import com.likeit.currenciesapp.ui.chat.redMessage.RedPlugin;
import com.likeit.currenciesapp.ui.chat.transferMessage.TransferPlugin;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.RongContext;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;


public class MyExtensionModule extends DefaultExtensionModule {
    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
//        List<IPluginModule> pluginModules =  getPluginModules(conversationType);
        List<IPluginModule> pluginModules =  super.getPluginModules(conversationType);
        //List<IPluginModule> pluginModuleList = new ArrayList<>();
       // pluginModules.add(new RedPlugin());
        pluginModules.add(new RedPlugin(RongContext.getInstance()));
        pluginModules.add(new TransferPlugin(RongContext.getInstance()));
       // return pluginModuleList;
        Log.d("TAG","pluginModules  :"+pluginModules.size());
            return pluginModules;
    }


//
//    public List<IPluginModule> getRongPluginModules(Conversation.ConversationType conversationType) {
//        ArrayList pluginModuleList = new ArrayList();
//        ImagePlugin image = new ImagePlugin();
//        FilePlugin file = new FilePlugin();
//        pluginModuleList.add(image);
//
//        if(conversationType.equals(Conversation.ConversationType.GROUP) || conversationType.equals(Conversation.ConversationType.DISCUSSION) || conversationType.equals(Conversation.ConversationType.PRIVATE)) {
//            pluginModuleList.addAll(InternalModuleManager.getInstance().getExternalPlugins(conversationType));
//        }
//
//        pluginModuleList.add(file);
//        return pluginModuleList;
//    }
}
