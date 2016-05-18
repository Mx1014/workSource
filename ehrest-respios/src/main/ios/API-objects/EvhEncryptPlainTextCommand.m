//
// EvhEncryptPlainTextCommand.m
//
#import "EvhEncryptPlainTextCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEncryptPlainTextCommand
//

@implementation EvhEncryptPlainTextCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEncryptPlainTextCommand* obj = [EvhEncryptPlainTextCommand new];
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
    if(self.plainText)
        [jsonObject setObject: self.plainText forKey: @"plainText"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.plainText = [jsonObject objectForKey: @"plainText"];
        if(self.plainText && [self.plainText isEqual:[NSNull null]])
            self.plainText = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
