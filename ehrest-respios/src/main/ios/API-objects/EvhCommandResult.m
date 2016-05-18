//
// EvhCommandResult.m
//
#import "EvhCommandResult.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommandResult
//

@implementation EvhCommandResult

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCommandResult* obj = [EvhCommandResult new];
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
    if(self.identifier)
        [jsonObject setObject: self.identifier forKey: @"identifier"];
    if(self.errorScope)
        [jsonObject setObject: self.errorScope forKey: @"errorScope"];
    if(self.errorCode)
        [jsonObject setObject: self.errorCode forKey: @"errorCode"];
    if(self.errorDescription)
        [jsonObject setObject: self.errorDescription forKey: @"errorDescription"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.identifier = [jsonObject objectForKey: @"identifier"];
        if(self.identifier && [self.identifier isEqual:[NSNull null]])
            self.identifier = nil;

        self.errorScope = [jsonObject objectForKey: @"errorScope"];
        if(self.errorScope && [self.errorScope isEqual:[NSNull null]])
            self.errorScope = nil;

        self.errorCode = [jsonObject objectForKey: @"errorCode"];
        if(self.errorCode && [self.errorCode isEqual:[NSNull null]])
            self.errorCode = nil;

        self.errorDescription = [jsonObject objectForKey: @"errorDescription"];
        if(self.errorDescription && [self.errorDescription isEqual:[NSNull null]])
            self.errorDescription = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
