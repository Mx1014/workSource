//
// EvhListAllFamilyMembersCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFamilyMemberFullDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAllFamilyMembersCommandResponse
//
@interface EvhListAllFamilyMembersCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhFamilyMemberFullDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

