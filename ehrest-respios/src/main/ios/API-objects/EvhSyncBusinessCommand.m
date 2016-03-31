//
// EvhSyncBusinessCommand.m
// generated at 2016-03-28 15:56:07 
//
#import "EvhSyncBusinessCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncBusinessCommand
//

@implementation EvhSyncBusinessCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSyncBusinessCommand* obj = [EvhSyncBusinessCommand new];
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
    [super toJson: jsonObject];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
