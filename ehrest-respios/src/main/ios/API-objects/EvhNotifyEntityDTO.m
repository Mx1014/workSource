//
// EvhNotifyEntityDTO.m
//
#import "EvhNotifyEntityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNotifyEntityDTO
//

@implementation EvhNotifyEntityDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhNotifyEntityDTO* obj = [EvhNotifyEntityDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.msg_sn)
        [jsonObject setObject: self.msg_sn forKey: @"msg_sn"];
    if(self.return_code)
        [jsonObject setObject: self.return_code forKey: @"return_code"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.msg_sn = [jsonObject objectForKey: @"msg_sn"];
        if(self.msg_sn && [self.msg_sn isEqual:[NSNull null]])
            self.msg_sn = nil;

        self.return_code = [jsonObject objectForKey: @"return_code"];
        if(self.return_code && [self.return_code isEqual:[NSNull null]])
            self.return_code = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
