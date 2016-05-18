//
// EvhGetPunchNewExceptionCommandResponse.m
//
#import "EvhGetPunchNewExceptionCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetPunchNewExceptionCommandResponse
//

@implementation EvhGetPunchNewExceptionCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetPunchNewExceptionCommandResponse* obj = [EvhGetPunchNewExceptionCommandResponse new];
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
    if(self.exceptionCode)
        [jsonObject setObject: self.exceptionCode forKey: @"exceptionCode"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.exceptionCode = [jsonObject objectForKey: @"exceptionCode"];
        if(self.exceptionCode && [self.exceptionCode isEqual:[NSNull null]])
            self.exceptionCode = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
