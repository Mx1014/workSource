//
// EvhDeleteHistoryByIdCommand.m
//
#import "EvhDeleteHistoryByIdCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteHistoryByIdCommand
//

@implementation EvhDeleteHistoryByIdCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteHistoryByIdCommand* obj = [EvhDeleteHistoryByIdCommand new];
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
    if(self.historyId)
        [jsonObject setObject: self.historyId forKey: @"historyId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.historyId = [jsonObject objectForKey: @"historyId"];
        if(self.historyId && [self.historyId isEqual:[NSNull null]])
            self.historyId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
