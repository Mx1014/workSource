//
// EvhListWaitApproveFamilyCommandResponse.h
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

