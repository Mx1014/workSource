//
// EvhListAdminOpRequestCommandResponse.h
// generated at 2016-04-07 17:33:48 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhGroupOpRequestDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAdminOpRequestCommandResponse
//
@interface EvhListAdminOpRequestCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhGroupOpRequestDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

