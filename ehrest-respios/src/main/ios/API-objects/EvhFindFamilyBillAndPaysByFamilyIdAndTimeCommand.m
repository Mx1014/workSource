//
// EvhFindFamilyBillAndPaysByFamilyIdAndTimeCommand.m
//
#import "EvhFindFamilyBillAndPaysByFamilyIdAndTimeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindFamilyBillAndPaysByFamilyIdAndTimeCommand
//

@implementation EvhFindFamilyBillAndPaysByFamilyIdAndTimeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFindFamilyBillAndPaysByFamilyIdAndTimeCommand* obj = [EvhFindFamilyBillAndPaysByFamilyIdAndTimeCommand new];
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
    if(self.billDate)
        [jsonObject setObject: self.billDate forKey: @"billDate"];
    if(self.familyId)
        [jsonObject setObject: self.familyId forKey: @"familyId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.billDate = [jsonObject objectForKey: @"billDate"];
        if(self.billDate && [self.billDate isEqual:[NSNull null]])
            self.billDate = nil;

        self.familyId = [jsonObject objectForKey: @"familyId"];
        if(self.familyId && [self.familyId isEqual:[NSNull null]])
            self.familyId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
