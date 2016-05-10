//
// EvhListPunchExceptionRequestCommandResponse.m
//
#import "EvhListPunchExceptionRequestCommandResponse.h"
#import "EvhPunchExceptionRequestDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPunchExceptionRequestCommandResponse
//

@implementation EvhListPunchExceptionRequestCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPunchExceptionRequestCommandResponse* obj = [EvhListPunchExceptionRequestCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _exceptionRequestList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageOffset)
        [jsonObject setObject: self.nextPageOffset forKey: @"nextPageOffset"];
    if(self.exceptionRequestList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPunchExceptionRequestDTO* item in self.exceptionRequestList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"exceptionRequestList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageOffset = [jsonObject objectForKey: @"nextPageOffset"];
        if(self.nextPageOffset && [self.nextPageOffset isEqual:[NSNull null]])
            self.nextPageOffset = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"exceptionRequestList"];
            for(id itemJson in jsonArray) {
                EvhPunchExceptionRequestDTO* item = [EvhPunchExceptionRequestDTO new];
                
                [item fromJson: itemJson];
                [self.exceptionRequestList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
