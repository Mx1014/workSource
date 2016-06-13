//
// EvhApprovePunchExceptionRequestCommand.m
//
#import "EvhApprovePunchExceptionRequestCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApprovePunchExceptionRequestCommand
//

@implementation EvhApprovePunchExceptionRequestCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhApprovePunchExceptionRequestCommand* obj = [EvhApprovePunchExceptionRequestCommand new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.processCode)
        [jsonObject setObject: self.processCode forKey: @"processCode"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.processCode = [jsonObject objectForKey: @"processCode"];
        if(self.processCode && [self.processCode isEqual:[NSNull null]])
            self.processCode = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
