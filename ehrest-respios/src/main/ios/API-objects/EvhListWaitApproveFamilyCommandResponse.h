//
// EvhListWaitApproveFamilyCommandResponse.h
// generated at 2016-04-05 13:45:25 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFamilyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListWaitApproveFamilyCommandResponse
//
@interface EvhListWaitApproveFamilyCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhFamilyDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

