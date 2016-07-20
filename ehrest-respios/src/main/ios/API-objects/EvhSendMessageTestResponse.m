//
// EvhSendMessageTestResponse.m
//
#import "EvhSendMessageTestResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendMessageTestResponse
//

@implementation EvhSendMessageTestResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSendMessageTestResponse* obj = [EvhSendMessageTestResponse new];
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
    if(self.text)
        [jsonObject setObject: self.text forKey: @"text"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.text = [jsonObject objectForKey: @"text"];
        if(self.text && [self.text isEqual:[NSNull null]])
            self.text = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
