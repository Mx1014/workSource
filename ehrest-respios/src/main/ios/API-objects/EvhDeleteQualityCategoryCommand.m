//
// EvhDeleteQualityCategoryCommand.m
//
#import "EvhDeleteQualityCategoryCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteQualityCategoryCommand
//

@implementation EvhDeleteQualityCategoryCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteQualityCategoryCommand* obj = [EvhDeleteQualityCategoryCommand new];
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
    if(self.categoryId)
        [jsonObject setObject: self.categoryId forKey: @"categoryId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.categoryId = [jsonObject objectForKey: @"categoryId"];
        if(self.categoryId && [self.categoryId isEqual:[NSNull null]])
            self.categoryId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
