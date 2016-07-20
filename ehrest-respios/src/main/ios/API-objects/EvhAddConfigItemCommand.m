//
// EvhAddConfigItemCommand.m
//
#import "EvhAddConfigItemCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddConfigItemCommand
//

@implementation EvhAddConfigItemCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddConfigItemCommand* obj = [EvhAddConfigItemCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _configProps = [NSMutableDictionary new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.configName)
        [jsonObject setObject: self.configName forKey: @"configName"];
    if(self.configType)
        [jsonObject setObject: self.configType forKey: @"configType"];
    if(self.configProps) {
        NSMutableDictionary* jsonMap = [NSMutableDictionary new];
        for(NSString* key in self.configProps) {
            [jsonMap setValue:[self.configProps objectForKey: key] forKey: key];
        }
        [jsonObject setObject: jsonMap forKey: @"configProps"];
    }        
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.configName = [jsonObject objectForKey: @"configName"];
        if(self.configName && [self.configName isEqual:[NSNull null]])
            self.configName = nil;

        self.configType = [jsonObject objectForKey: @"configType"];
        if(self.configType && [self.configType isEqual:[NSNull null]])
            self.configType = nil;

        {
            NSDictionary* jsonMap = [jsonObject objectForKey: @"configProps"];
            for(NSString* key in jsonMap) {
                [self.configProps setObject: [jsonMap objectForKey: key] forKey: key];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
